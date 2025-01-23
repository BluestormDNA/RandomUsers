package io.github.bluestormdna.randomusers.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import io.github.bluestormdna.randomusers.domain.repositories.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsersViewModel(
    scope: CoroutineScope,
    private val userRepository: UserRepository,
): ViewModel(scope) {

    private val _search = MutableStateFlow("")
    val search = _search.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val users = search
        .map { search -> search.isNotEmpty() }
        .distinctUntilChanged()
        .flatMapLatest { searching ->
            if (searching) filteredLocalUsers else cachedRemotePagedUsers
    }

    private val cachedRemotePagedUsers = userRepository.users
        .cachedIn(viewModelScope)

    private val filteredLocalUsers = userRepository.localUsers
        .cachedIn(viewModelScope)
        .combine(search) { pagingData, search ->
            pagingData.filter { user ->
                user.firstName.contains(search)
                        || user.lastName.contains(search)
                        || user.email.contains(search)
            }
        }.cachedIn(viewModelScope)

    fun onUserSearch(search: String) {
        _search.update { search }
    }

    fun onClearSearch() {
        _search.update { "" }
    }

    fun onDeleteUser(uuid: String) = viewModelScope.launch {
        userRepository.deleteUser(uuid)
    }
}