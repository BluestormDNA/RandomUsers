package io.github.bluestormdna.randomusers.ui.userdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import io.github.bluestormdna.randomusers.domain.model.User
import io.github.bluestormdna.randomusers.domain.repositories.UserRepository
import io.github.bluestormdna.randomusers.ui.navigation.UserDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class UserDetailsViewModel(
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val userId = savedStateHandle.toRoute<UserDetails>().id

    val user = MutableStateFlow(userId)
        .map { userRepository.getUser(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, User.EMPTY)
}