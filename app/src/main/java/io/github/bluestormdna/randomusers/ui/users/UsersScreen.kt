package io.github.bluestormdna.randomusers.ui.users

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import io.github.bluestormdna.randomusers.R
import io.github.bluestormdna.randomusers.domain.model.Location
import io.github.bluestormdna.randomusers.domain.model.User
import io.github.bluestormdna.randomusers.ui.theme.DismissRed
import io.github.bluestormdna.randomusers.ui.theme.RandomUsersTheme
import kotlinx.coroutines.flow.flowOf
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UsersScreenRoute(
    onUserDetail: (String) -> Unit,
) {
    val vm = koinViewModel<UsersViewModel>()
    val users = vm.users.collectAsLazyPagingItems()
    val search by vm.search.collectAsState()

    UsersScreen(
        users = users,
        search = search,
        onUserSearch = vm::onUserSearch,
        onClearSearch = vm::onClearSearch,
        onUserDelete = vm::onDeleteUser,
        onUserDetail = onUserDetail,
    )
}

@PreviewLightDark
@Composable
private fun UserScreenPreview() {
    RandomUsersTheme {
        Surface {
            UsersScreen(
                users = flowOf(PagingData.from(previewUsers)).collectAsLazyPagingItems(),
                search = "",
                onUserSearch = {},
                onClearSearch = {},
                onUserDetail = {},
                onUserDelete = {},
            )
        }
    }
}

@Composable
fun UsersScreen(
    users: LazyPagingItems<User>,
    search: String,
    onUserSearch: (String) -> Unit,
    onClearSearch: () -> Unit,
    onUserDetail: (String) -> Unit,
    onUserDelete: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        ClearableTextField(
            search = search,
            onUserSearch = onUserSearch,
            onClearSearch = onClearSearch,
        )
        Box(modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp),
            ) {
                items(count = users.itemCount, key = users.itemKey { user -> user.uuid }) { index ->
                    val user = users[index] ?: return@items
                    SwipeToDismissUserCard(
                        user = user,
                        onClick = onUserDetail,
                        onSwipe = onUserDelete,
                        modifier = Modifier.animateItem()
                    )
                }
                when (users.loadState.append) {
                    is LoadState.Error -> item { LoadingError { users.retry() } }
                    LoadState.Loading -> {
                        if (users.itemCount != 0) {
                            item { LoadingMoreContent() }
                        }
                    }

                    is LoadState.NotLoading -> Unit
                }
            }
            when (users.loadState.refresh) {
                is LoadState.Error -> {
                    androidx.compose.animation.AnimatedVisibility(
                        modifier = Modifier.align(Alignment.Center),
                        visible = users.loadState.refresh is LoadState.Error
                    ) {
                        Column(
                            modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            LoadingError {
                                users.retry()
                            }
                        }
                    }
                }

                LoadState.Loading -> {
                    androidx.compose.animation.AnimatedVisibility(
                        modifier = Modifier.align(Alignment.Center),
                        visible = users.loadState.refresh == LoadState.Loading
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is LoadState.NotLoading -> Unit
            }
        }
    }
}

@Composable
fun LoadingError(
    text: String = stringResource(R.string.there_was_an_error_loading_data),
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text)
        Spacer(Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun LoadingMoreContent(
    text: String = stringResource(R.string.loading)
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        text = text
    )
}

@Composable
fun ClearableTextField(
    search: String,
    onUserSearch: (String) -> Unit,
    onClearSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        value = search,
        onValueChange = onUserSearch,
        placeholder = { Text(stringResource(R.string.search)) },
        singleLine = true,
        trailingIcon = {
            IconButton(
                onClick = {
                    onClearSearch()
                    focusManager.clearFocus()
                },
                content = { Icon(Icons.Default.Clear, null) }
            )
        },
        keyboardActions = KeyboardActions(onAny = { focusManager.clearFocus() })
    )
}

@PreviewLightDark
@Composable
private fun UserCardPreview(
    user: User = previewUsers[0]
) {
    RandomUsersTheme {
        Surface {
            UserCard(
                user = user,
                onClick = {},
            )
        }
    }
}

@Composable
fun SwipeToDismissUserCard(
    user: User,
    onClick: (String) -> Unit,
    onSwipe: (String) -> Unit,
    modifier: Modifier,
) {
    val configuration = LocalConfiguration.current
    val dismissWidth = configuration.screenWidthDp.dp / 3
    val density = LocalDensity.current
    val eventHorizon = with(density) { dismissWidth.toPx() }
    val swipeState = rememberSwipeToDismissBoxState(
        positionalThreshold = { eventHorizon },
        confirmValueChange = { change ->
            when (change) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onSwipe(user.uuid)
                    true
                }

                SwipeToDismissBoxValue.StartToEnd,
                SwipeToDismissBoxValue.Settled -> false
            }
        },
    )
    SwipeToDismissBox(
        modifier = modifier,
        state = swipeState,
        enableDismissFromStartToEnd = false,
        backgroundContent = { SwipeToDismissBackground() }
    ) {
        UserCard(
            user = user,
            onClick = onClick,
        )
    }
}

@PreviewLightDark
@Composable
fun SwipeToDismissBackground() {
    Card(
        Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DismissRed),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                modifier = Modifier.padding(end = 16.dp),
                imageVector = Icons.Default.Delete,
                tint = Color.White,
                contentDescription = null
            )
        }
    }
}

@Composable
fun UserCard(
    user: User,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = { onClick(user.uuid) })
                .padding(16.dp),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.image)
                    .crossfade(true)
                    .build(),
                modifier = Modifier
                    .size(80.dp)
                    .border(
                        width = 2.dp,
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface,
                    )
                    .shadow(2.dp, CircleShape)
                    .background(MaterialTheme.colorScheme.surface),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text("${user.firstName} ${user.lastName}")
                Text(user.email)
                Text(user.phone)
            }
        }
    }
}

val previewUsers = List(10) { index ->
    User(
        uuid = "uuid $index",
        gender = "gender $index",
        firstName = "firstName $index",
        lastName = "lastName $index",
        location = Location(
            street = "street $index",
            city = "city $index",
            state = "state $index",
        ),
        registeredDate = "date $index",
        email = "email@$index.com",
        phone = "123 $index",
        image = "",
        thumbnail = "",
    )
}