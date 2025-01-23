package io.github.bluestormdna.randomusers.data.local

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.github.bluestormdna.randomusers.UserFactory
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var database: UserDatabase
    private lateinit var userDao: UserDao

    private val users = UserFactory.generateEntity(10)

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, UserDatabase::class.java).build()
        userDao = database.userDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertUsers_and_Read_returns_FullDataSet() = runTest {

        userDao.insertUsers(users)
        val pagingSource = userDao.getUsersPagingSource()
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )
        assertTrue((loadResult as PagingSource.LoadResult.Page).data.containsAll(users))
    }

    @Test
    fun insertUser_can_be_retrieved() = runTest {
        val user = users[0]
        userDao.insertUsers(listOf(user))
        val retrievedUser = userDao.getUser("uuid 0")
        assertEquals(user, retrievedUser)
    }

    @Test
    fun markAsDelete_sets_visible_0() = runTest {
        val user = users[0]
        userDao.insertUsers(listOf(user))
        userDao.markAsDeleted("uuid 0")
        val retrievedUser = userDao.getUser("uuid 0")
        assertEquals(false, retrievedUser.visible)
    }

    @Test
    fun markAsDelete_makes_fullDataSet_narrower() = runTest {
        val user = users[0]
        userDao.insertUsers(listOf(user))
        userDao.markAsDeleted("uuid 0")
        val pagingSource = userDao.getUsersPagingSource()
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )
        assertFalse((loadResult as PagingSource.LoadResult.Page).data.containsAll(users))
    }

    @Test
    fun deleteAll_deletes_All() = runTest {
        userDao.insertUsers(users)
        userDao.deleteAll()
        val pagingSource = userDao.getUsersPagingSource()
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )
        assertTrue((loadResult as PagingSource.LoadResult.Page).data.isEmpty())
    }
}
