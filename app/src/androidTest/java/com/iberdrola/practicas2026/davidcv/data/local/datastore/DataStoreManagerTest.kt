package com.iberdrola.practicas2026.davidcv.data.local.datastore

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import com.iberdrola.practicas2026.davidcv.domain.model.account.Account
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DataStoreManagerTest {

    private lateinit var dataStoreManager: DataStoreManager
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val gson = Gson()

    @Before
    fun setUp() {
        dataStoreManager = DataStoreManager(context, gson)
    }

    @Test
    fun saveAndReadAccount() = runBlocking {
        // Given
        val account = Account(1, "Test User", "test@test.com", null)
        
        // When
        dataStoreManager.saveAccount(account)
        val savedAccount = dataStoreManager.account.first()
        
        // Then
        assertEquals(account.name, savedAccount?.name)
        assertEquals(account.email, savedAccount?.email)
    }

    @Test
    fun clearAccount() = runBlocking {
        // Given
        val account = Account(1, "Test User", "test@test.com", null)
        dataStoreManager.saveAccount(account)
        
        // When
        dataStoreManager.clearAccount()
        val savedAccount = dataStoreManager.account.first()
        
        // Then
        assertNull(savedAccount)
    }

    @Test
    fun saveAndReadBsCounter() = runBlocking {
        // When
        dataStoreManager.saveBsCounter(5)
        val counter = dataStoreManager.bsCounter.first()
        
        // Then
        assertEquals(5, counter)
    }
}
