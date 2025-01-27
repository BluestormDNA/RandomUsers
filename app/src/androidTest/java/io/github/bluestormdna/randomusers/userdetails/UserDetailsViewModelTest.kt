package io.github.bluestormdna.randomusers.userdetails

import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import io.github.bluestormdna.randomusers.UserFactory
import io.github.bluestormdna.randomusers.domain.repositories.UserRepository
import io.github.bluestormdna.randomusers.ui.userdetails.UserDetailsViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDetailsViewModelTest {

    private lateinit var userRepository: UserRepository

    private lateinit var viewModel: UserDetailsViewModel

    private val testUsers = UserFactory.generate(10)

    @Before
    fun setup() {
        userRepository = mock {
            everySuspend { getUser(any()) } returns testUsers[1]
        }

        val savedStateHandle = SavedStateHandle(mapOf("id" to testUsers[1].uuid))

        viewModel = UserDetailsViewModel(userRepository, savedStateHandle)
    }

    @Test
    fun when_viewmodel_starts_it_fetches_the_specified_user() = runTest {
        val user = viewModel.user.value
        assert(user == testUsers[1])
    }
}