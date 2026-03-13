package com.iberdrola.practicas2026.davidcv.ui.screens.billlist

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iberdrola.practicas2026.davidcv.domain.exception.BillException
import com.iberdrola.practicas2026.davidcv.ui.base.screens.EmptyBillsScreen
import com.iberdrola.practicas2026.davidcv.ui.base.screens.ErrorScreen

/**
 * BillListContent
 * Gestiona el estado de la UI (Carga, Error, Éxito) para el listado de facturas
 *
 * @param state
 * @param modifier
 * @param onErrorClick
 */
@Composable
fun BillListContent(
    state: BillListState,
    modifier: Modifier,
    onErrorClick: () -> Unit,
    onEmptyClick: () -> Unit
) {
    when (state) {
        is BillListState.Loading -> {
            BillListContentEmpty(modifier = modifier)
        }
        is BillListState.Error -> {
            ErrorScreen(
                message = state.exception.message ?: "Error desconocido",
                modifier = modifier,
                img = if (state.exception is BillException.ConexionFailed) Icons.Default.WifiOff else Icons.Default.Error,
                onClick = {
                    onErrorClick()
                }
            )
        }
        is BillListState.Success -> {
            val bills = state.bills
            if (bills.isEmpty()) {
                EmptyBillsScreen(
                    modifier = modifier,
                    onRefresh = {
                        onEmptyClick()
                    }
                )
            } else {
                BillListContentInfo(
                    bills = bills,
                    modifier = modifier
                )
            }
        }
    }
}
