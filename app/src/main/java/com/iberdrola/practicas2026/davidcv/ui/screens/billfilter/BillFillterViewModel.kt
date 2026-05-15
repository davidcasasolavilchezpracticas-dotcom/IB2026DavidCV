package com.iberdrola.practicas2026.davidcv.ui.screens.billfilter

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.model.bill.Bill
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.usecase.GetGasBillsUseCase
import com.iberdrola.practicas2026.davidcv.domain.usecase.GetLightBillsUseCase
import com.iberdrola.practicas2026.davidcv.ui.base.common.dfValidateDate
import com.iberdrola.practicas2026.davidcv.ui.screens.billlist.BillListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class BillViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _initialFiltersKey = "initial_filters"
    
    // Límites de respaldo
    private var minLimit: Float? = null
    private var maxLimit: Float? = null
    private var minDateLimit: LocalDateTime? = null
    private var maxDateLimit: LocalDateTime? = null

    private val _state = MutableStateFlow(
        savedStateHandle.get<BillFilterState>(_initialFiltersKey) ?: BillFilterState()
    )
    val state: StateFlow<BillFilterState> = _state.asStateFlow()


    init {
        validatePriceRange()
    }


    fun setInitialFilters(
        filters: BillFilterState,
        min: Float? = null,
        max: Float? = null,
        minDate: LocalDateTime? = null,
        maxDate: LocalDateTime? = null
    ) {
        this.minLimit = min
        this.maxLimit = max
        this.minDateLimit = minDate
        this.maxDateLimit = maxDate
        
        _state.update {
            val newFilters = if (filters.priceRange == null && min != null && max != null) {
                filters.copy(priceRange = min..max)
            } else filters
            newFilters
        }
        validatePriceRange()
    }

    private fun validatePriceRange() {
        val max = _state.value.priceRange?.endInclusive ?: return
        val min = _state.value.priceRange?.start ?: return
        val currentRange = _state.value.priceRange

        if (currentRange == null) {
            _state.update { it.copy(priceRange = min..max) }
        } else {
            val safeStart = currentRange.start.coerceIn(min, max)
            val safeEnd = currentRange.endInclusive.coerceIn(safeStart, max)
            if (safeStart != currentRange.endInclusive || safeEnd != currentRange.start) {
                _state.update { it.copy(priceRange = safeStart..safeEnd) }
            }
        }
    }

    fun deleteFilters() {
        _state.update { 
            val base = BillFilterState()
            if (minLimit != null && maxLimit != null) {
                base.copy(priceRange = minLimit!!..maxLimit!!)
            } else base
        }
    }

    // region Gestión de Estados de UI

    fun onPriceRangeChanged(range: ClosedFloatingPointRange<Float>) {
        _state.update { it.copy(priceRange = range) }
    }

    fun onStateChangePaid(v: Boolean) = updateFilter { copy(paymentStatusPaid = v) }
    fun onStateChangePending(v: Boolean) = updateFilter { copy(paymentStatusPending = v) }
    fun onStateChangeTramited(v: Boolean) = updateFilter { copy(paymentStatusTramited = v) }
    fun onStateChangeCanceled(v: Boolean) = updateFilter { copy(paymentStatusCanceled = v) }
    fun onStateChangeFixed(v: Boolean) = updateFilter { copy(paymentStatusFixed = v) }

    private fun updateFilter(update: BillFilterState.() -> BillFilterState) {
        _state.update(update)
    }

    // endregion

    // region Gestión de Fechas

    fun onStartDateSelected(date: String, context: Context) {
        parseDate(date)?.let { parsed ->
            if (_state.value.endDate == null || _state.value.endDate!! > parsed) {
                _state.update { it.copy(startDate = parsed) }
            } else {
                Toast.makeText(context, R.string.bfvmToastStartDate, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onEndDateSelected(date: String, context: Context) {
        parseDate(date)?.let { parsed ->
            if (_state.value.startDate == null || _state.value.startDate!! < parsed) {
                _state.update { it.copy(endDate = parsed) }
            } else {
                Toast.makeText(context, R.string.bfvmToastEndDate, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun parseDate(date: String): LocalDateTime? = try {
        LocalDate.parse(date, dfValidateDate).atStartOfDay()
    } catch (e: Exception) { null }

    fun onValidStartDate(date: String): Boolean = parseDate(date)?.let {
        _state.value.endDate == null || _state.value.endDate!! > it
    } ?: false

    // endregion
}
