package com.iberdrola.practicas2026.davidcv.ui.navigation

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.data.local.datastore.DataStoreManager
import com.iberdrola.practicas2026.davidcv.domain.model.account.Account
import com.iberdrola.practicas2026.davidcv.ui.screens.useraccount.UserAccountState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    val defaultAccount = Account(
        id = 0,
        name = "Julian",
        email = "julian@gmail.com",
        profileImage = R.drawable.profile_picture.toString()
    )

    val bsCounter: StateFlow<Int> = dataStoreManager.bsCounter
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val uiState: StateFlow<UserAccountState> = dataStoreManager.account
        .map { account -> 
            UserAccountState(account = account ?: defaultAccount)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserAccountState(isLoading = true)
        )

    // Mantener 'account' para compatibilidad o simplificar si se prefiere
    val account: StateFlow<Account?> = dataStoreManager.account
        .map { savedAccount -> savedAccount ?: defaultAccount }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun updateBsCounter(counter: Int) {
        viewModelScope.launch {
            dataStoreManager.saveBsCounter(counter)
        }
    }

    fun saveAccount(account: Account) {
        viewModelScope.launch {
            val persistentImage = when (val image = account.profileImage) {
                is Uri -> image.toString()
                else -> image?.toString()
            }
            dataStoreManager.saveAccount(account.copy(profileImage = persistentImage))
        }
    }

    fun saveImageToInternalStorage(context: Context, uri: Uri): Uri? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileName = "profile_${UUID.randomUUID()}.jpg"
            val file = File(context.filesDir, fileName)

            val outputStream = FileOutputStream(file)
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            Uri.fromFile(file)
        } catch (e: Exception) {
            null
        }
    }

    fun logout() {
        viewModelScope.launch {
            dataStoreManager.clearAccount()
        }
    }
}
