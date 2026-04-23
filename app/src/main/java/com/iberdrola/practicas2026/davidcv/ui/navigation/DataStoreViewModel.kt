package com.iberdrola.practicas2026.davidcv.ui.navigation

import android.content.Context
import android.net.Uri
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.data.local.datastore.DataStoreManager
import com.iberdrola.practicas2026.davidcv.domain.model.account.Account
import com.iberdrola.practicas2026.davidcv.ui.screens.useraccount.UserAccountState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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

    private val defaultAccount = Account(
        id = 0,
        name = "Julian",
        email = "julian@gmail.com",
        profileImage = R.drawable.profile_picture
    )

    // Estados temporales para la edición
    private val _editingName = MutableStateFlow<String?>(null)
    private val _editingEmail = MutableStateFlow<String?>(null)
    private val _editingProfileImage = MutableStateFlow<Any?>(null)

    val bsCounter: StateFlow<Int> = dataStoreManager.bsCounter
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val trys: StateFlow<Int> = dataStoreManager.trys
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 3
        )


    /**
     * Estado consolidado para la UI de cuenta de usuario.
     */
    val uiState: StateFlow<UserAccountState> = combine(
        dataStoreManager.account,
        _editingName,
        _editingEmail,
        _editingProfileImage
    ) { account, editName, editEmail, editImage ->
        val currentAccount = account ?: defaultAccount
        
        val name = editName ?: currentAccount.name
        val email = editEmail ?: currentAccount.email
        
        // Lógica para recuperar la imagen correctamente
        val profileImage = editImage ?: transformProfileImage(currentAccount.profileImage)

        UserAccountState(
            account = currentAccount,
            name = name,
            email = email,
            profileImage = profileImage,
            isEmailValid = email.isEmpty() || Patterns.EMAIL_ADDRESS.matcher(email).matches(),
            isNameValid = name.length >= 3
        )
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserAccountState(isLoading = true)
    )

    /**
     * GSON puede deserializar los IDs de recursos (Int) como Double o String.
     * Esta función asegura que Coil reciba un tipo que pueda manejar.
     */
    private fun transformProfileImage(image: Any?): Any? {
        return when (image) {
            is String -> {
                // Si parece una URI de archivo o contenido, la devolvemos como tal
                if (image.startsWith("content://") || image.startsWith("file://") || image.startsWith("/")) {
                    Uri.parse(image)
                } else {
                    // Si es un String numérico (ID de recurso guardado por error), intentamos pasarlo a Int
                    image.toIntOrNull() ?: image
                }
            }
            is Double -> image.toInt() // GSON a veces convierte Int a Double en Any
            else -> image ?: R.drawable.profile_picture
        }
    }

    val account: StateFlow<Account?> = dataStoreManager.account
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = defaultAccount
        )

    fun onNameChange(newName: String) {
        _editingName.value = newName
    }

    fun onEmailChange(newEmail: String) {
        _editingEmail.value = newEmail
    }

    fun onImageChange(newImage: Any?) {
        _editingProfileImage.value = newImage
    }

    fun updateBsCounter(counter: Int) {
        viewModelScope.launch {
            dataStoreManager.saveBsCounter(counter)
        }
    }

    fun updateTrys(trys: Int) {
        viewModelScope.launch {
            dataStoreManager.saveTrys(trys)
        }
    }

    /**
     * Guarda la cuenta actualizada y limpia los estados temporales de edición.
     */
    fun saveAccount(account: Account) {
        viewModelScope.launch {
            val persistentImage = when (val image = account.profileImage) {
                is Uri -> image.toString()
                is Int -> image // Mantener el Int para recursos
                else -> image
            }
            dataStoreManager.saveAccount(account.copy(profileImage = persistentImage))
            
            _editingName.value = null
            _editingEmail.value = null
            _editingProfileImage.value = null
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
}
