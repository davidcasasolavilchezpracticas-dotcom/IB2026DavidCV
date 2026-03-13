package com.iberdrola.practicas2026.davidcv.ui.screens.billlist

import com.iberdrola.practicas2026.davidcv.domain.exception.BillException
import com.iberdrola.practicas2026.davidcv.domain.model.Bill

/**
 * Gestiona el estado de la pantalla BilllList
 */
sealed class BillListState {
    data object Loading : BillListState()
    data class Error(val exception: BillException) : BillListState()
    data class Success(val bills: List<Bill>) : BillListState()
}