package io.github.bluestormdna.randomusers.users

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import io.github.bluestormdna.randomusers.UserFactory
import io.github.bluestormdna.randomusers.domain.repositories.UserRepository
import io.github.bluestormdna.randomusers.ui.users.UsersViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class UsersViewModelTest {

    private lateinit var userRepository: UserRepository

    private lateinit var viewModel: UsersViewModel

    private val testUsers = UserFactory.generate(DEFAULT_TEST_USERS)

    @Before
    fun setup() {
        userRepository = mock {
            every { users } returns flowOf(PagingData.from(testUsers))
            every { localUsers } returns flowOf(PagingData.from(testUsers))
        }
        val dispatcher = UnconfinedTestDispatcher()
        val testScope = CoroutineScope(dispatcher)
        viewModel = UsersViewModel(testScope, userRepository)
    }

    @Test
    fun `users flow should contain cached users`() = runTest {
        val pagingData = viewModel.users.first()
        val snapshot = flowOf(pagingData).asSnapshot()

        assertEquals(DEFAULT_TEST_USERS, snapshot.size)
    }

    @Test
    fun `on search name should return filtered data`() = runTest {
        val filter = "firstName 1"
        viewModel.onUserSearch(filter)

        val pagingData = viewModel.users.first()
        val snapshot = flowOf(pagingData).asSnapshot()
        val dataSetCount = testUsers.count { user -> user.firstName.contains(filter) }
        assertEquals(dataSetCount, snapshot.size)
    }

    @Test
    fun `on search surname should return filtered data`() = runTest {
        val filter = "lastName 3"
        viewModel.onUserSearch(filter)

        val pagingData = viewModel.users.first()
        val snapshot = flowOf(pagingData).asSnapshot()
        val dataSetCount = testUsers.count { user -> user.lastName.contains(filter) }
        assertEquals(dataSetCount, snapshot.size)
    }

    @Test
    fun `on search email should return filtered data`() = runTest {
        val filter = "44.com"
        viewModel.onUserSearch(filter)

        val pagingData = viewModel.users.first()
        val snapshot = flowOf(pagingData).asSnapshot()
        val dataSetCount = testUsers.count { user -> user.email.contains(filter) }
        assertEquals(dataSetCount, snapshot.size)
    }

    @Test
    fun `on search and on clear returns full cached dataset`() = runTest {
        val filter = "firstName 1"
        viewModel.onUserSearch(filter)
        val pagingDataFiltered = viewModel.users.first()
        viewModel.onClearSearch()
        val fullDataSet = viewModel.users.first()

        val filteredSnapshot = flowOf(pagingDataFiltered).asSnapshot()
        val fullSnapshot = flowOf(fullDataSet).asSnapshot()

        assertNotEquals(filteredSnapshot.size, fullSnapshot.size)
    }

    companion object {
        const val DEFAULT_TEST_USERS = 100
    }

}