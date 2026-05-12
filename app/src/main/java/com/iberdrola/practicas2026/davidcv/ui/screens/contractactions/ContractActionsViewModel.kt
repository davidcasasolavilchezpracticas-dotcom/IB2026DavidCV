package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.usecase.GetContractByIdUseCase
import com.iberdrola.practicas2026.davidcv.domain.usecase.UpdateContractEmailAndStatusUseCase
import com.iberdrola.practicas2026.davidcv.domain.usecase.UpdateContractEmailUseCase
import com.iberdrola.practicas2026.davidcv.domain.usecase.UpdateContractPhoneUseCase
import com.iberdrola.practicas2026.davidcv.domain.usecase.UpdateContractStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.abs
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds


@HiltViewModel
class ContractActionsViewModel @Inject constructor(
    private val _updateContractEmailAndStatusUseCase: UpdateContractEmailAndStatusUseCase,
    private val _updateContractEmailUseCase: UpdateContractEmailUseCase,
    private val _updateContractPhoneUseCase: UpdateContractPhoneUseCase,
    private val _getContractByIdUseCase: GetContractByIdUseCase,
    private val _updateContractStatusUseCase: UpdateContractStatusUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ContractActionsState())
    val state: StateFlow<ContractActionsState> = _state.asStateFlow()

    fun onLoadEnd() {
        viewModelScope.launch {
            delay(Random.nextLong(1000, 3000))
            _state.update { it.copy(isLoading = false) }
        }
    }

    // region OnChange
    fun onEmailChanged(email: String) {
        _state.update { it.copy(emailTry = email) }
    }

    fun onPhoneChanged(phone: String) {
        _state.update { it.copy(phoneTry = phone) }
    }

    fun onAcceptedChanged(accepted: Boolean) {
        _state.update { it.copy(isAcceptedPolicy = accepted) }
    }

    fun onVerifyCodeChanged(code: String) {
        if (code.length <= 6)
            _state.update { it.copy(verifyCodeTry = code) }
    }

    // endregion


    //region Code
    fun generateNewCode() {
        _state.update { it.copy(verifyCode = Random.nextInt(99999, 999999).toString(), isLoading = true) }
    }

    //endregion

    //region Censurator

    fun censurator(email: String) : String{
        val censuredText = "*****"
        if(email.isNotEmpty()){
            if (email.length <= 7)
                return (email.substring(0, 1) + censuredText + email.substring((email.lastIndexOf('@') - 1),email.length))
            else
                return StringBuilder().append(email.substring(0, 1) + censuredText + email.substring((email.lastIndexOf('@') - 1), email.length - 1)).toString()
        }
        return "a*****z@gmail.com"
    }

    fun phoneCensurator(phone: String) : String {
        val censuredText = "******"
        return censuredText + if (state.value.contract?.phone != null) state.value.contract?.phone?.substring((state.value.contract?.phone?.length ?: 9) - 4) else "123"
    }

    //endregion

    //region Updates

    fun updateContractEmail(email: String) {
        viewModelScope.launch {
            if ( _updateContractEmailUseCase(_state.value.contract?.id!!, email) is BaseResult.Success) {
                Log.d("ComprobacionesContractActionsViewModel", "Email actualizado correctamente")
                _state.update { it.copy(emailChanged = true) }
            } else {
                _state.update { it.copy(errorMessage = "Error al actualizar el email") }
            }
        }
    }

    fun updateContractPhone(phone: String) {
        viewModelScope.launch {
            if ( _updateContractPhoneUseCase(_state.value.contract?.id!!, phone) is BaseResult.Success) {
                Log.d("ComprobacionesContractActionsViewModel", "Número de teléfono actualizado correctamente")
                _state.update { it.copy(phoneChanged = true) }
            } else {
                _state.update { it.copy(errorMessage = "Error al actualizar el número de teléfono") }
            }
        }
    }

    fun updateContractStatus(status: ContractStatus) {
        viewModelScope.launch {
            if ( _updateContractStatusUseCase(_state.value.contract?.id!!, status) is BaseResult.Success) {
                Log.d("ComprobacionesContractActionsViewModel", "Status actualizado correctamente")
                _state.update { it.copy(emailChanged = true) }
            } else {
                _state.update { it.copy(errorMessage = "Error al actualizar el email") }
            }
        }
    }

    fun updateContractEmailAndStatus(email: String, status: ContractStatus) {
        viewModelScope.launch {
            if ( _updateContractEmailAndStatusUseCase(_state.value.contract?.id!!, email, status) is BaseResult.Success) {
                Log.d("ComprobacionesContractActionsViewModel", "Email y status actualizados correctamente")
                _state.update { it.copy(emailChanged = true) }
            } else {
                _state.update { it.copy(errorMessage = "Error al actualizar el email") }
            }
        }
    }

    //endregion

    //region Get Contracts

    fun getContract(id: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            _getContractByIdUseCase(id).collect { result ->
                when (result) {
                    is BaseResult.Success -> {
                        Log.d("Comprobaciones", "Contract -> ${result.data}")
                        _state.value = _state.value.copy(
                            isLoading = false,
                            contract = result.data,
                            action = if(result.data.status == ContractStatus.ACTIVE)
                                    state.value.action
                                else
                                    ContractActions.MODIFYSTATUSEMAIL
                        )
                        Log.d("Comprobaciones", "Contract -> ${_state.value.contract}")
                    }
                    is BaseResult.Error -> {
                        Log.d("Comprobaciones", "Contract -> ${result.exception}")
                        _state.update { it.copy(
                            isLoading = false,
                            errorMessage = result.exception.message ?: "Error desconocido"
                        ) }
                    }
                }
            }
        }
    }

    //endregion

    //region Time Left

    fun getTimeLeft(context: Context) : Boolean {
        val sharedPref = context.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)

        val lastEjecution = sharedPref.getLong("start_time", 0L)
        val nextEjecution = lastEjecution + TimeUnit.HOURS.toMillis(12)
        val timeLeft = abs(System.currentTimeMillis() - nextEjecution)

        Log.d("Comprobaciones", "Last Ejecution -> $lastEjecution")
        Log.d("Comprobaciones", "Next Ejecution -> $nextEjecution")
        Log.d("Comprobaciones", "Time Left -> $timeLeft")

        _state.update { it.copy(
            timeLeftToResend = if (timeLeft > 0) {
                    String.format(
                        Locale.getDefault(), "%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(timeLeft),
                        TimeUnit.MILLISECONDS.toMinutes(timeLeft) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(timeLeft) % 60 % 60
                    )
                } else {
                    "Se están reiniciando los intentos"
                }
            )
        }
        return timeLeft > 0
    }

    fun createText(
        value: Boolean,
        txt1: String,
        txt2: String
    ) {
        _state.update {
            it.copy(
                timeLeftToResend = if (value)
                    txt1 + state.value.timeLeftToResend + txt2
                else
                    state.value.timeLeftToResend
            )
        }
    }

    //endregion
}
