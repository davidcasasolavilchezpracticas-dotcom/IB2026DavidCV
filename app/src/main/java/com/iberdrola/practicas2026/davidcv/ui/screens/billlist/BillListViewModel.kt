package com.iberdrola.practicas2026.davidcv.ui.screens.billlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.usecase.GetGasBillsUseCase
import com.iberdrola.practicas2026.davidcv.domain.usecase.GetLightBillsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class BillListViewModel @Inject constructor(
    private val _getLightBillsUseCase: GetLightBillsUseCase,
    private val _getGasBillsUseCase: GetGasBillsUseCase,
) : ViewModel() {

    private val _billsState = MutableStateFlow<BillListState>(BillListState.Loading)
    val billsState: StateFlow<BillListState> = _billsState


    fun getLightBills() {
        viewModelScope.launch {
            _billsState.value = BillListState.Loading
            delay(Random.nextLong(1000, 3000))
            _getLightBillsUseCase().collect { billsList ->
                when (billsList) {
                    is BaseResult.Success -> {
                        _billsState.value = BillListState.Success(billsList.data)
                    }
                    is BaseResult.Error -> {
                        Log.d("Comprobaciones", "Pasa por Error: ${billsList.exception.message}")
                        _billsState.value = BillListState.Error(billsList.exception)
                    }
                }
            }
        }
    }

    fun getGasBills() {
        viewModelScope.launch {
            _billsState.value = BillListState.Loading
            delay(Random.nextLong(1000, 3000))
            _getGasBillsUseCase().collect { billsList ->
                when (billsList) {
                    is BaseResult.Success -> {
                        _billsState.value = BillListState.Success(billsList.data)
                    }
                    is BaseResult.Error -> {
                        _billsState.value = BillListState.Error(billsList.exception)
                    }
                }
            }
        }
    }

}