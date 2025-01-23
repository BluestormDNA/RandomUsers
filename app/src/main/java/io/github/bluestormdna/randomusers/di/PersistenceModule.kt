package io.github.bluestormdna.randomusers.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import io.github.bluestormdna.randomusers.data.local.UserDatabase
import io.github.bluestormdna.randomusers.data.preferences.DataStoreManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val persistenceModule = module {
    singleOf(::provideDatabase)
    singleOf(::provideUserDao)
    single { androidContext().dataStore }
    singleOf(::DataStoreManager)
}

// Datastore is a singleton by AndroidX design
private val Context.dataStore by preferencesDataStore("settings")

private fun provideUserDao(database: UserDatabase) = database.userDao()

private fun provideDatabase(
    context: Context
) = Room.databaseBuilder(
    context,
    UserDatabase::class.java,
    UserDatabase.DATABASE_NAME,
).fallbackToDestructiveMigration()
    .build()