package io.github.bluestormdna.randomusers.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import io.github.bluestormdna.randomusers.data.local.UserDao
import io.github.bluestormdna.randomusers.data.local.entities.UserEntity
import io.github.bluestormdna.randomusers.data.preferences.DataStoreManager
import io.github.bluestormdna.randomusers.data.remote.api.RandomUserApi
import io.github.bluestormdna.randomusers.data.remote.dto.Result
import io.github.bluestormdna.randomusers.data.remote.dto.toEntity
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator(
    private val randomUserApi: RandomUserApi,
    private val userDao: UserDao,
    private val datastore: DataStoreManager,
) : RemoteMediator<Int, UserEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    userDao.deleteAll()
                    datastore.clear()
                    STARTING_PAGE_INDEX
                }

                LoadType.PREPEND -> return MediatorResult.Success(true)
                LoadType.APPEND -> {
                    val lastCachedPage = datastore.lastUserCachedPage.first()
                    val userTotalPages = datastore.userTotalPages.first()

                    if (lastCachedPage == userTotalPages) {
                        return MediatorResult.Success(true)
                    }

                    lastCachedPage + 1
                }
            }

            val paginationSeed = datastore.getSeed().toString()
            val response = randomUserApi.getRandomUsers(page, 20, paginationSeed)
            datastore.setLastUserCachedPage(page)
            datastore.setUserTotalPages(response.info.results)
            userDao.insertUsers(response.results.map(Result::toEntity))

            MediatorResult.Success(false)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        val remoteKey = datastore.lastUserCachedPage.first()
        return if (remoteKey == STARTING_PAGE_INDEX) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}