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
import com.iberdrola.practicas2026.davidcv.domain.model.account.Account
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

    companion object {
        val BS_COUNTER_KEY = intPreferencesKey("bs_counter")
        val TRYS_KEY = intPreferencesKey("verif_trys")
        val ACCOUNT_KEY = stringPreferencesKey("account_json")
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

    //endregion trys


    val bsCounter: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[BS_COUNTER_KEY] ?: 0
    }

    /**
     * Account es un Flow que emite los cambios guardados en DataStore.
     */
    val account: Flow<Account?> = context.dataStore.data.map { preferences ->
        val json = preferences[ACCOUNT_KEY]
        if (json != null) {
            try {
                gson.fromJson(json, Account::class.java)
            } catch (e: Exception) {
                Log.d("ComprobacionesDataStoreManager", "Error al deserializar el JSON de la cuenta: ${e.message}")
                null
            }
        } else null
    }

    suspend fun saveBsCounter(counter: Int) {
        context.dataStore.edit { preferences ->
            preferences[BS_COUNTER_KEY] = counter
        }
    }

    /**
     * Esta es la ÚNICA forma de modificar la cuenta.
     * Al editar el DataStore, el Flow 'account' emitirá automáticamente el nuevo valor.
     */
    suspend fun saveAccount(account: Account) {
        context.dataStore.edit { preferences ->
            preferences[ACCOUNT_KEY] = gson.toJson(account)
            Log.d("ComprobacionesDataStoreManager", "JSON guardado en DataStore: ${preferences[ACCOUNT_KEY]}")
        }
    }

    suspend fun clearAccount() {
        context.dataStore.edit { preferences ->
            preferences.remove(ACCOUNT_KEY)
        }
    }
}
