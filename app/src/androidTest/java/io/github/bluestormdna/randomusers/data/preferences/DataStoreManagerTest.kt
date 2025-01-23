package io.github.bluestormdna.randomusers.data.preferences

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith

private val Context.dataStore by preferencesDataStore("settings")

@RunWith(AndroidJUnit4::class)
class DataStoreManagerTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val dataStoreManager = DataStoreManager(context.dataStore)

    @After
    fun tearDown() {
        runBlocking {
            dataStoreManager.clear()
        }
    }

    @Test
    fun defaultLastUserCachedPage_is_One() = runTest {
        val lastUserCachedPage = dataStoreManager.lastUserCachedPage.first()

        assertEquals(1, lastUserCachedPage)
    }

    @Test
    fun defaultUserTotalPages_is_Zero() = runTest {
        val userTotalPages = dataStoreManager.userTotalPages.first()

        assertEquals(0, userTotalPages)
    }

    @Test
    fun lastUserCachedPage_return_same_as_savedLastUserCachedPage() = runTest {
        val page = 123
        dataStoreManager.setLastUserCachedPage(page)

        val lastUserPage = dataStoreManager.lastUserCachedPage.first()

        assertEquals(page, lastUserPage)
    }

    @Test
    fun userTotalPages_returns_same_as_saveUserTotalPages() = runTest {
        val total = 50
        dataStoreManager.setUserTotalPages(total)

        val savedTotal = dataStoreManager.userTotalPages.first()

        assertEquals(total, savedTotal)
    }

    @Test
    fun clear_clears_persisted_data() = runTest {
        val total = 50
        dataStoreManager.setUserTotalPages(total)
        dataStoreManager.clear()

        val savedLastPage = dataStoreManager.lastUserCachedPage.first()

        assertNotEquals(total, savedLastPage)
    }

    @Test
    fun getSeed_is_idempotent() = runTest {
        val seedList = List(3) {
            dataStoreManager.getSeed()
        }

        val seedRepeatedCount = seedList.groupBy { it }.count()

        assertEquals(1, seedRepeatedCount)
    }

    @Test
    fun getSeed_generates_new_seed_after_clear() = runTest {
        val seed = dataStoreManager.getSeed()
        dataStoreManager.clear()
        val newSeed = dataStoreManager.getSeed()

        assertNotEquals(seed, newSeed)
    }

}