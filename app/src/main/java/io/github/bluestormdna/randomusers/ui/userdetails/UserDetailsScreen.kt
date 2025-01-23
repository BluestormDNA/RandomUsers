package io.github.bluestormdna.randomusers.ui.userdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import io.github.bluestormdna.randomusers.domain.model.User
import io.github.bluestormdna.randomusers.ui.theme.RandomUsersTheme
import io.github.bluestormdna.randomusers.ui.users.previewUsers
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UserDetailsScreenRoute() {
    val vm = koinViewModel<UserDetailsViewModel>()
    val user by vm.user.collectAsState()

    UserDetailsScreen(user)
}

@PreviewLightDark
@Composable
private fun UserDetailsScreenPreview() {
    RandomUsersTheme {
        Surface {
            UserDetailsScreen(previewUsers[0])
        }
    }
}

@Composable
fun UserDetailsScreen(user: User) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxSize()
        ) {
            AsyncImage(
                model = user.image,
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    )
                    .padding(32.dp),
                text = "${user.firstName} ${user.lastName}",
                fontSize = 32.sp,
            )
        }
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(user.gender)
            Text(user.email)
            Text(user.registeredDate)
            Text("${user.location.street} ${user.location.city} ${user.location.state}")
        }
    }
}