package com.iberdrola.practicas2026.davidcv.ui.screens.billlist

import androidx.compose.foundation.pager.PagerState
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
import kotlinx.coroutines.Job
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

    private var currentFilters: BillFilterState = BillFilterState()

    private var allLightBills: List<Bill>? = null
    private var allGasBills: List<Bill>? = null

    private var lightBillsJob: Job? = null
    private var gasBillsJob: Job? = null

    /**
     * Devuelve los filtros aplicados actualmente.
     */
    fun getCurrentFilters(): BillFilterState = currentFilters

    /**
     * applyFilters
     * Solo actualiza el estado de la UI si ya tenemos datos cargados.
     * Si estamos cargando, solo guarda los filtros para usarlos cuando lleguen los datos.
     */
    fun applyFilters(filters: BillFilterState) {
        currentFilters = filters

        if (_lightBillsState.value is BillListState.Success && allLightBills != null) {
            _lightBillsState.value =
                BillListState.Success(filterList(allLightBills!!, currentFilters))
        }

        if (_gasBillsState.value is BillListState.Success && allGasBills != null) {
            _gasBillsState.value =
                BillListState.Success(filterList(allGasBills!!, currentFilters))
        }
    }

    private fun filterList(list: List<Bill>, filters: BillFilterState): List<Bill> {
        return list.filter { bill ->
            // Filtro por Fecha
            val matchDate = (filters.startDate == null || !bill.startDate.isBefore(filters.startDate)) &&
                    (filters.endDate == null || !bill.endDate.isAfter(filters.endDate))

            // Filtro por Precio
            val matchPrice = filters.priceRange?.let { range ->
                bill.value in range
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

    fun refreshBills(currentState: PagerState){
        when (currentState.currentPage){
            0 -> {
                getLightBills()
                getGasBills()
            }
            1 -> {
                getGasBills()
                getLightBills()
            }
        }
    }

    /**
     * getLightBills
     * Obtiene las facturas de luz. Controlado para evitar parpadeos en refresco.
     */
    fun getLightBills() {
        lightBillsJob?.cancel()
        lightBillsJob = viewModelScope.launch {
            // Solo ponemos Loading si no estamos ya en Success (evita parpadeo en refresh)
            val wasAlreadyLoaded = allLightBills != null
            if (!wasAlreadyLoaded) {
                _lightBillsState.value = BillListState.Loading
            }
            
            delay(Random.nextLong(1000, 3000))
            _getLightBillsUseCase().collect { result ->
                when (result) {
                    is BaseResult.Success -> {
                        if (wasAlreadyLoaded && result.data.isEmpty() && !allLightBills.isNullOrEmpty()) {
                            return@collect
                        }

                        allLightBills = result.data
                        _lightBillsState.value = BillListState.Success(filterList(result.data, currentFilters))
                    }
                    is BaseResult.Error -> {
                        _lightBillsState.value = BillListState.Error(result.exception as BillException)
                    }
                }
            }
        }
    }

    /**
     * getGasBills
     * Obtiene las facturas de gas. Controlado para evitar parpadeos en refresco.
     */
    fun getGasBills() {
        gasBillsJob?.cancel()
        gasBillsJob = viewModelScope.launch {
            val wasAlreadyLoaded = allGasBills != null
            if (!wasAlreadyLoaded) {
                _gasBillsState.value = BillListState.Loading
            }
            
            delay(Random.nextLong(1000, 3000))
            _getGasBillsUseCase().collect { result ->
                when (result) {
                    is BaseResult.Success -> {
                        if (wasAlreadyLoaded && result.data.isEmpty() && !allGasBills.isNullOrEmpty()) {
                            return@collect
                        }

                        allGasBills = result.data
                        _gasBillsState.value = BillListState.Success(filterList(result.data, currentFilters))
                    }
                    is BaseResult.Error -> {
                        _gasBillsState.value = BillListState.Error(result.exception as BillException)
                    }
                }
            }
        }
    }
}
