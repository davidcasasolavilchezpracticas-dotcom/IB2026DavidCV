package com.iberdrola.practicas2026.davidcv.ui.screens.billlist

import androidx.compose.foundation.pager.PagerState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.iberdrola.practicas2026.davidcv.domain.exception.BillException
import com.iberdrola.practicas2026.davidcv.domain.model.bill.Bill
import com.iberdrola.practicas2026.davidcv.domain.model.bill.PaymentStatus
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.usecase.GetGasBillsUseCase
import com.iberdrola.practicas2026.davidcv.domain.usecase.GetLightBillsUseCase
import com.iberdrola.practicas2026.davidcv.ui.base.common.dfNormalBill
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.horizontalpage.useLocal
import com.iberdrola.practicas2026.davidcv.ui.screens.billfilter.BillFilterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
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
     * Obtiene los límites de precio de todas las facturas cargadas.
     */
    fun getPriceLimits(): Pair<Float, Float>? {
        val all = (allLightBills.orEmpty()) + (allGasBills.orEmpty())
        if (all.isEmpty()) return null
        return all.minOf { it.value } to all.maxOf { it.value }
    }

    /**
     * Obtiene los límites de fecha de todas las facturas cargadas.
     */
    fun getDateLimits(): Pair<LocalDateTime, LocalDateTime>? {
        val all = (allLightBills.orEmpty()) + (allGasBills.orEmpty())
        if (all.isEmpty()) return null
        return all.minOf { it.emisionDate } to all.maxOf { it.emisionDate }
    }

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

    fun comprobateStatus() : List<String> {
        val seleccionados = listOfNotNull(
            if (currentFilters.paymentStatusPaid) "Pagado" else null,
            if (currentFilters.paymentStatusPending) "Pendiente" else null,
            if (currentFilters.paymentStatusTramited) "En trámite de cobro" else null,
            if (currentFilters.paymentStatusCanceled) "Anulada" else null,
            if (currentFilters.paymentStatusFixed) "Cuota fija" else null
        )

        return seleccionados
    }

    fun filtersActives() : List<String> {
        val filters = mutableListOf<String>()
        if (currentFilters.startDate != null) filters.add("Fecha mínima de emisión: ${currentFilters.startDate!!.format(dfNormalBill)}")
        if (currentFilters.endDate != null) filters.add("Fecha máxima de emisión: ${currentFilters.endDate!!.format(dfNormalBill)}")
        if (currentFilters.priceRange != null &&
            (
                currentFilters.priceRange?.start != getPriceLimits()?.first ||
                currentFilters.priceRange?.endInclusive != getPriceLimits()?.second
            )
        ) { filters.add("Rango de precio: ${kotlin.math.truncate(currentFilters.priceRange!!.start)} - ${kotlin.math.ceil(currentFilters.priceRange!!.endInclusive)}") }

        val seleccionados = comprobateStatus()

        if (seleccionados.isNotEmpty()) filters.add("Estados seleccionados: ${seleccionados.joinToString(", ")}")

        return filters
    }

    companion object {
        fun countFilters(cFilters: BillFilterState, min: Float, max: Float) : Int {
            var c = 0
            if (cFilters.startDate != null) c++
            if (cFilters.endDate != null) c++
            if (cFilters.priceRange != null && ( cFilters.priceRange.start != min || cFilters.priceRange.endInclusive != max )) c++
            if (cFilters.paymentStatusPaid) c++
            if (cFilters.paymentStatusPending) c++
            if (cFilters.paymentStatusTramited) c++
            if (cFilters.paymentStatusCanceled) c++
            if (cFilters.paymentStatusFixed) c++
            return c
        }

        fun alertDialogText(
            filtersActives: () -> List<String>
        ) : String {
            val filters = filtersActives()
            return if (filters.isNotEmpty()) {
                if(filters.size == 1) "Se ha seleccionado el siguiente filtro:\n" + filters.joinToString("\n")
                else "Se han seleccionado los siguientes filtros:\n" + filters.joinToString("\n")
            } else {
                "No hay filtros seleccionados"
            }
        }
        fun filterList(list: List<Bill>, filters: BillFilterState): List<Bill> {
            return list.filter { bill ->
                // Filtro por Fecha
                val matchDate =
                    (filters.startDate == null || !bill.startDate.isBefore(filters.startDate)) &&
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

    fun onDeleteFilters() {
        applyFilters(BillFilterState())
    }

    fun onErrorClick(
        onRefresh: () -> Unit,
        currentState: BillListState,
        navController: NavController,
        useLocal: (BillListState) -> Unit,
    ) {
        if (currentState is BillListState.Error && currentState.exception is BillException.ConexionFailed){
            useLocal(currentState)
            navController.popBackStack()
        } else {
            onRefresh()
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
            _getLightBillsUseCase().collect { result ->
                when (result) {
                    is BaseResult.Success -> {
                        allLightBills = result.data
                        _lightBillsState.value = BillListState.Success(filterList(result.data, currentFilters))
                    }
                    is BaseResult.Error -> {
                        _lightBillsState.value = BillListState.Error(result.exception as BillException)
                    }
                }
            }

            delay(Random.nextLong(1000, 3000))
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
            _getGasBillsUseCase().collect { result ->
                when (result) {
                    is BaseResult.Success -> {
                        allGasBills = result.data
                        _gasBillsState.value = BillListState.Success(filterList(result.data, currentFilters))
                    }
                    is BaseResult.Error -> {
                        _gasBillsState.value = BillListState.Error(result.exception as BillException)
                    }
                }
            }
            delay(Random.nextLong(1000, 3000))
        }
    }
}
