package com.iberdrola.practicas2026.davidcv.ui.screens.billfilter

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.exception.BillException
import com.iberdrola.practicas2026.davidcv.ui.base.common.dfValidateDate
import com.iberdrola.practicas2026.davidcv.ui.base.common.localeEs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class BillViewModel @Inject constructor(

) : ViewModel() {
    private var _state = MutableStateFlow(BillFilterState())
    val state: StateFlow<BillFilterState> = _state


    fun deleteFilters() {
        _state.value = BillFilterState()
    }

    //region Dates

    fun onStartDateSelected(date: String, context: Context) {
        try {
            val parsedDate = LocalDate.parse(date, dfValidateDate).atStartOfDay()
            if(_state.value.endDate == null || _state.value.endDate!! > parsedDate)
            {
                _state.value = _state.value.copy(startDate = parsedDate)
            } else
            {
                throw BillException.DateInvalid
            }
        } catch (e: Exception) {
            Log.e("ComprobacionesBillViewModel", "Error parsing start date: $date", e)
        }catch (e: BillException.DateInvalid) {
            Log.e("ComprobacionesBillViewModel", "${e.message}")
            Toast.makeText(context, R.string.bfvmToastStartDate, Toast.LENGTH_SHORT).show()
        }
    }

    fun onEndDateSelected(date: String, context: Context) {
        Log.d("Comprobaciones", "Pasa por onEndDateSelected: $date")
        try {
            val parsedDate = LocalDate.parse(date, dfValidateDate).atStartOfDay()
            if(_state.value.startDate == null || _state.value.startDate!! < parsedDate)
            {
                _state.value = _state.value.copy(endDate = parsedDate)
                Log.d("Comprobaciones", "endDate actualizado: ${_state.value.endDate}")
            } else
            {
                throw BillException.DateInvalid
            }
        } catch (e: Exception) {
            Log.e("ComprobacionesBillViewModel", "Error parsing end date: $date", e)
        }catch (e: BillException.DateInvalid) {
            Log.e("ComprobacionesBillViewModel", "${e.message}")
            Toast.makeText(context, R.string.bfvmToastEndDate, Toast.LENGTH_SHORT).show()
        }
    }

    fun onValidEndDate(date: String): Boolean {
        val parsedDate = LocalDate.parse(date, dfValidateDate).atStartOfDay()
        return _state.value.startDate == null || _state.value.startDate!! < parsedDate
    }

    fun onValidStartDate(date: String): Boolean {
        val parsedDate = LocalDate.parse(date, dfValidateDate).atStartOfDay()
        return _state.value.endDate == null || _state.value.endDate!! > parsedDate
    }
    //endregion

    //region Prize

    fun onPriceRangeChanged(range: ClosedFloatingPointRange<Float>) {
        _state.value = _state.value.copy(priceRange = range)
    }

    //endregion

    //region States

    fun onStateChangePaid(value: Boolean) {
        _state.value = _state.value.copy(paymentStatusPaid = value)
    }

    fun onStateChangePending(value: Boolean) {
        _state.value = _state.value.copy(paymentStatusPending = value)
    }

    fun onStateChangeTramited(value: Boolean) {
        _state.value = _state.value.copy(paymentStatusTramited = value)
    }

    fun onStateChangeCanceled(value: Boolean) {
        _state.value = _state.value.copy(paymentStatusCanceled = value)
    }

    fun onStateChangeFixed(value: Boolean) {
        _state.value = _state.value.copy(paymentStatusFixed = value)
    }

    //endregion

}