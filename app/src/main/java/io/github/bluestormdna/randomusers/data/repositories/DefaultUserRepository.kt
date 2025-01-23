package io.github.bluestormdna.randomusers.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import io.github.bluestormdna.randomusers.data.local.UserDao
import io.github.bluestormdna.randomusers.data.local.entities.UserEntity
import io.github.bluestormdna.randomusers.data.paging.UserRemoteMediator
import io.github.bluestormdna.randomusers.domain.model.User
import io.github.bluestormdna.randomusers.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class DefaultUserRepository(
    private val userDao: UserDao,
    userRemoteMediator: UserRemoteMediator,
): UserRepository {
    @OptIn(ExperimentalPagingApi::class)
    override val users: Flow<PagingData<User>> = Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 2, initialLoadSize = 40),
            remoteMediator = userRemoteMediator,
            pagingSourceFactory = { userDao.getUsersPagingSource() },
        ).flow
            .map { pagingData -> pagingData.map(UserEntity::toDomain) }

    override val localUsers: Flow<PagingData<User>> = Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 2, initialLoadSize = 40),
            pagingSourceFactory = { userDao.getUsersPagingSource() },
        ).flow
            .map { pagingData -> pagingData.map(UserEntity::toDomain) }

    override suspend fun getUser(id: String): User {
        return userDao.getUser(id).toDomain()
    }

    override suspend fun deleteUser(uuid: String) {
        userDao.markAsDeleted(uuid)
    }

}