package io.github.bluestormdna.randomusers.data.local

import androidx.paging.PagingSource
import androidx.room.*
import io.github.bluestormdna.randomusers.data.local.entities.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsers(user: List<UserEntity>)

    @Query("SELECT * FROM user_table WHERE visible == 1")
    fun getUsersPagingSource(): PagingSource<Int, UserEntity>

    @Query("SELECT * FROM user_table WHERE uuid LIKE :uuid")
    suspend fun getUser(uuid: String): UserEntity

    @Query("UPDATE user_table SET visible = 0 WHERE uuid LIKE :uuid")
    suspend fun markAsDeleted(uuid: String)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()
}