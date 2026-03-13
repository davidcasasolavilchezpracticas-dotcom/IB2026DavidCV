package com.iberdrola.practicas2026.davidcv.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * DataStoreManager
 * Clase para acceder a los datos almacenados en DataStore
 *
 */
@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        val BS_COUNTER_KEY = intPreferencesKey("bs_counter")
    }

    val bsCounter: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[BS_COUNTER_KEY] ?: 0
    }

    suspend fun saveBsCounter(counter: Int) {
        context.dataStore.edit { preferences ->
            preferences[BS_COUNTER_KEY] = counter
        }
    }
}
