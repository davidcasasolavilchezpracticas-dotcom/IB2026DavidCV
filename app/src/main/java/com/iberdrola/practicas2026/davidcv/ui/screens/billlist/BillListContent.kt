package com.iberdrola.practicas2026.davidcv.ui.screens.billlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    onErrorClick: () -> Unit
) {
    when (state) {
        is BillListState.Loading -> {
            BillListContentEmpty(modifier = modifier)
        }
        is BillListState.Error -> {
            ErrorScreen(
                message = state.message.message ?: "Error desconocido",
                modifier = modifier,
                onClick = {
                    onErrorClick()
                }
            )
        }
        is BillListState.Success -> {
            val bills = state.bills
            if (bills.isEmpty()) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No tienes facturas disponibles",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            } else {
                BillListContentInfo(
                    bills = bills,
                    modifier = modifier
                )
            }
        }
    }
}
