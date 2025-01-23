package io.github.bluestormdna.randomusers.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.random.Random

class DataStoreManager(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun getSeed(): Int {
        val seed = dataStore.data.first()[PAGINATION_SEED]

        return if (seed != null) {
            seed
        } else {
            val newSeed = Random.nextInt()
            dataStore.edit { preferences -> preferences[PAGINATION_SEED] = newSeed }
            newSeed
        }
    }

    suspend fun setLastUserCachedPage(page: Int) {
        dataStore.edit { preferences ->
            preferences[USER_LAST_CACHED_PAGE] = page
        }
    }

    val lastUserCachedPage: Flow<Int> = dataStore.data.map { preferences ->
        preferences[USER_LAST_CACHED_PAGE] ?: 1
    }

    suspend fun setUserTotalPages(total: Int) {
        dataStore.edit { preferences ->
            preferences[USER_TOTAL_PAGES] = total
        }
    }

    val userTotalPages: Flow<Int> = dataStore.data.map { preferences ->
        preferences[USER_TOTAL_PAGES] ?: 0
    }

    suspend fun clear() = dataStore.edit { preferences -> preferences.clear() }

    companion object {
        val USER_LAST_CACHED_PAGE = intPreferencesKey("USER_LAST_CACHED_PAGE")
        val USER_TOTAL_PAGES = intPreferencesKey("USER_TOTAL_PAGES")
        val PAGINATION_SEED = intPreferencesKey("PAGINATION_SEED")
    }

}