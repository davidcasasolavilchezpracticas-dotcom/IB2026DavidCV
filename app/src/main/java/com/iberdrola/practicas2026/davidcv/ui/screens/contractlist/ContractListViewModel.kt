package com.iberdrola.practicas2026.davidcv.ui.screens.contractlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.davidcv.domain.exception.ContractException
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.usecase.GetContractsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

/**
 * ContractListViewModel
 * ViewModel para la pantalla de listado de contratos
 */
@HiltViewModel
class ContractListViewModel @Inject constructor(
    private val _getContractsUseCase: GetContractsUseCase
) : ViewModel() {

    private val _contractsState = MutableStateFlow<ContractListState>(ContractListState.Loading)
    val contractsState: StateFlow<ContractListState> = _contractsState

    init {
        getContracts()
    }


    fun getContracts() {
        viewModelScope.launch {
            _contractsState.value = ContractListState.Loading

            delay(Random.nextLong(1000, 1500))
            
            _getContractsUseCase().collect { result ->
                when (result) {
                    is BaseResult.Success -> {
                        _contractsState.value = ContractListState.Success(result.data)
                    }
                    is BaseResult.Error -> {
                        val contractException = result.exception as? ContractException
                            ?: ContractException.UnknownError(result.exception.message)
                        _contractsState.value = ContractListState.Error(contractException)
                    }
                }
            }
        }
    }
}
