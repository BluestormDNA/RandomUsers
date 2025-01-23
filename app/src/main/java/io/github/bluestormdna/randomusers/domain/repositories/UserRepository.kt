package io.github.bluestormdna.randomusers.domain.repositories

import androidx.paging.PagingData
import io.github.bluestormdna.randomusers.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val users: Flow<PagingData<User>>
    val localUsers: Flow<PagingData<User>>
    suspend fun getUser(id: String): User
    suspend fun deleteUser(uuid: String)
}