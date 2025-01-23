package io.github.bluestormdna.randomusers.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.bluestormdna.randomusers.data.local.entities.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        const val DATABASE_NAME = "user_db"
    }
}