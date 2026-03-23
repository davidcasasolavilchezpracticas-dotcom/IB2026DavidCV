package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.usecase.GetContractByIdUseCase
import com.iberdrola.practicas2026.davidcv.domain.usecase.UpdateContractEmailAndStatusUseCase
import com.iberdrola.practicas2026.davidcv.domain.usecase.UpdateContractEmailUseCase
import com.iberdrola.practicas2026.davidcv.domain.usecase.UpdateContractPhoneUseCase
import com.iberdrola.practicas2026.davidcv.domain.usecase.UpdateContractStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random


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

    fun onLoad(loading: Boolean) {
        _state.update { it.copy(isLoading = loading) }/*todo implementar la carga*/
    }

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
        _state.update { it.copy(verifyCodeTry = code) }
    }

    fun generateNewCode() {
        _state.update { it.copy(verifyCode = Random.nextInt(99999, 999999).toString()) }
        Log.d("ComprobacionesContractActionsViewModel", "Codigo = ${_state.value.verifyCode}")
    }

    fun censurator(email: String) : String{
        if(email.isNotEmpty()){
            if (email.length <= 7)
                return String.format("*", 5) + email.substring(
                    (email.lastIndexOf('@') - 1),
                    email.length
                )
            else
                return StringBuilder(
                    email.substring(0, 1) + String.format(
                        "*",
                        5
                    ) + email.substring((email.lastIndexOf('@') - 1), email.length - 1)
                ).toString()
        }
        return "a*****z@gmail.com"
    }

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

    fun getContract(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            _getContractByIdUseCase(id).collect { result ->
                when (result) {
                    is BaseResult.Success -> {
                        _state.update { it.copy(isLoading = false, contract = result.data, action = if(result.data.status == ContractStatus.ACTIVE) ContractActions.MODIFYEMAIL else ContractActions.MODIFYSTATUSEMAIL) }
                    }
                    is BaseResult.Error -> {
                        _state.update { it.copy(
                            isLoading = false,
                            errorMessage = result.exception.message ?: "Error desconocido"
                        ) }
                    }
                }
            }
        }
    }
}
