package com.iberdrola.practicas2026.davidcv.ui.screens.billlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.exception.BillException
import com.iberdrola.practicas2026.davidcv.ui.base.screens.EmptyBillsScreen
import com.iberdrola.practicas2026.davidcv.ui.base.screens.ErrorScreen
import com.iberdrola.practicas2026.davidcv.ui.theme.EnergyGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * BillListContent
 * Gestiona el estado de la UI (Carga, Error, Éxito) para el listado de facturas
 *
 * @param state
 * @param modifier
 * @param onErrorClick
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillListContent(
    events: BillListEvents,
    state: BillListState,
    modifier: Modifier,
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    PullToRefreshBox (
        isRefreshing = isRefreshing,
        onRefresh = {
            events.onRefresh()
            isRefreshing = true
            scope.launch {
                delay(2000)
                isRefreshing = false
            }
        },
        modifier = modifier
    ){
        when (state) {
            is BillListState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = EnergyGreen)
                }
            }

            is BillListState.Error -> {
                ErrorScreen(
                    exception = state.exception,
                    message = state.exception.message ?: R.string.blcUnknownError.toString(),
                    modifier = modifier.verticalScroll(rememberScrollState()),
                    img = if (state.exception is BillException.ConexionFailed) Icons.Default.WifiOff else Icons.Default.Error,
                    onClick = {
                        events.onErrorClick(state)
                    }
                )
            }

            is BillListState.Success -> {
                val bills = state.bills

                if (bills.isEmpty()) {
                    EmptyBillsScreen(
                        modifier = modifier.verticalScroll(rememberScrollState()),
                        onRefresh = {
                            events.onEmptyClick()
                        }
                    )
                } else {
                    BillListContentInfo(
                        bills = bills,
                        modifier = modifier,
                        onFilterClick = events.onFilterClick
                    )
                }
            }
        }
    }
}
