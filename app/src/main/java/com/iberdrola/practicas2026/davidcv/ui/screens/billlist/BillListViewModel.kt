package com.iberdrola.practicas2026.davidcv.ui.screens.billlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.davidcv.domain.exception.BillException
import com.iberdrola.practicas2026.davidcv.domain.model.bill.Bill
import com.iberdrola.practicas2026.davidcv.domain.model.bill.PaymentStatus
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.usecase.GetGasBillsUseCase
import com.iberdrola.practicas2026.davidcv.domain.usecase.GetLightBillsUseCase
import com.iberdrola.practicas2026.davidcv.ui.screens.billfilter.BillFilterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random


/**
 * BillListViewModel
 * ViewModel para la pantalla de listado de facturas
 *
 * @param _getLightBillsUseCase
 * @param _getGasBillsUseCase
 */
@HiltViewModel
class BillListViewModel @Inject constructor(
    private val _getLightBillsUseCase: GetLightBillsUseCase,
    private val _getGasBillsUseCase: GetGasBillsUseCase,
) : ViewModel() {

    private val _lightBillsState = MutableStateFlow<BillListState>(BillListState.Loading)
    val lightBillsState: StateFlow<BillListState> = _lightBillsState

    private val _gasBillsState = MutableStateFlow<BillListState>(BillListState.Loading)
    val gasBillsState: StateFlow<BillListState> = _gasBillsState

    // Guardamos el filtro actual para aplicarlo siempre sobre los datos cargados
    private var currentFilters: BillFilterState = BillFilterState()

    // Listas originales para poder filtrar siempre sobre el total
    private var allLightBills: List<Bill> = emptyList()
    private var allGasBills: List<Bill> = emptyList()

    /**
     * applyFilters
     * Aplica los filtros recibidos a las listas originales y actualiza el estado
     */
    fun applyFilters(filters: BillFilterState) {
        currentFilters = filters
        _lightBillsState.value = BillListState.Success(filterList(allLightBills, currentFilters))
        _gasBillsState.value = BillListState.Success(filterList(allGasBills, currentFilters))
    }

    private fun filterList(list: List<Bill>, filters: BillFilterState): List<Bill> {
        return list.filter { bill ->
            // Filtro por Fecha
            val matchDate = (filters.startDate == null || !bill.startDate.isBefore(filters.startDate)) &&
                    (filters.endDate == null || !bill.endDate.isAfter(filters.endDate))

            // Filtro por Precio
            val matchPrice = filters.priceRange?.let { range ->
                bill.value.toFloat() in range
            } ?: true

            // Filtro por Estado (Si no hay ninguno marcado, se muestran todos)
            val anyStatusSelected = filters.paymentStatusPaid || filters.paymentStatusPending ||
                    filters.paymentStatusTramited || filters.paymentStatusCanceled || filters.paymentStatusFixed

            val matchStatus = if (!anyStatusSelected) {
                true
            } else {
                when (bill.paymentStatus) {
                    PaymentStatus.PAID -> filters.paymentStatusPaid
                    PaymentStatus.PENDING -> filters.paymentStatusPending
                    PaymentStatus.TRAMITED -> filters.paymentStatusTramited
                    PaymentStatus.CANCELED -> filters.paymentStatusCanceled
                    PaymentStatus.FIXED_PAYMENT -> filters.paymentStatusFixed
                }
            }

            matchDate && matchPrice && matchStatus
        }
    }

    /**
     * getLightBills
     * Obtiene las facturas de luz
     */
    fun getLightBills() {
        viewModelScope.launch {
            _lightBillsState.value = BillListState.Loading
            delay(Random.nextLong(1000, 3000))
            _getLightBillsUseCase().collect { billsList ->
                when (billsList) {
                    is BaseResult.Success -> {
                        allLightBills = billsList.data
                        // Aplicamos el filtro actual (que puede ser el por defecto si no se ha filtrado)
                        _lightBillsState.value = BillListState.Success(filterList(allLightBills, currentFilters))
                    }
                    is BaseResult.Error -> {
                        _lightBillsState.value = BillListState.Error(billsList.exception as BillException)
                    }
                }
            }
        }
    }

    /**
     * getGasBills
     * Obtiene las facturas de gas
     */
    fun getGasBills() {
        viewModelScope.launch {
            _gasBillsState.value = BillListState.Loading
            delay(Random.nextLong(1000, 3000))
            _getGasBillsUseCase().collect { billsList ->
                when (billsList) {
                    is BaseResult.Success -> {
                        allGasBills = billsList.data
                        // Aplicamos el filtro actual
                        _gasBillsState.value = BillListState.Success(filterList(allGasBills, currentFilters))
                    }
                    is BaseResult.Error -> {
                        _gasBillsState.value = BillListState.Error(billsList.exception as BillException)
                    }
                }
            }
        }
    }
}
