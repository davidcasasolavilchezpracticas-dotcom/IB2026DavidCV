package com.iberdrola.practicas2026.davidcv.data.local.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {
    private val _maxTrys = 3

    companion object {
        val BS_COUNTER_KEY = intPreferencesKey("bs_counter")
        val TRYS_KEY = intPreferencesKey("verif_trys")
    }

    //region trys

    val trys: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[TRYS_KEY] ?: 3
    }

    suspend fun saveTrys(trys: Int) {
        context.dataStore.edit { preferences ->
            preferences[TRYS_KEY] = trys
        }
    }

    suspend fun restartTrys() {
        context.dataStore.edit { preferences ->
            preferences[TRYS_KEY] = _maxTrys
        }
    }

    //endregion trys


    val bsCounter: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[BS_COUNTER_KEY] ?: 0
    }

    suspend fun saveBsCounter(counter: Int) {
        context.dataStore.edit { preferences ->
            preferences[BS_COUNTER_KEY] = counter
        }
    }
}
