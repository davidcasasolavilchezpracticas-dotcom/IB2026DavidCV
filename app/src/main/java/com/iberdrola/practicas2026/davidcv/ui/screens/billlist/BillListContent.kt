package com.iberdrola.practicas2026.davidcv.ui.screens.billlist

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.exception.BillException
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.screens.EmptyBillsScreen
import com.iberdrola.practicas2026.davidcv.ui.base.screens.ErrorScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.billfilter.BillFilterState
import com.iberdrola.practicas2026.davidcv.ui.theme.EnergyGreen
import com.iberdrola.practicas2026.davidcv.ui.theme.White
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
    val pullState = rememberPullToRefreshState()
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
        state = pullState,
        modifier = modifier,
        indicator = {
            PullToRefreshDefaults.Indicator(
                state = pullState,
                isRefreshing = isRefreshing,
                modifier = Modifier.align(Alignment.TopCenter),
                containerColor = White,
                color = EnergyGreen
            )
        }
    ){
        when (state) {
            is BillListState.Loading -> {
                BillListContentEmpty(modifier = modifier)
            }

            is BillListState.Error -> {
                ErrorScreen(
                    exception = state.exception,
                    message = state.exception.message ?: R.string.blcUnknownError.toString(),
                    modifier = modifier.verticalScroll(rememberScrollState()),
                    img = if (state.exception is BillException.ConexionFailed) Icons.Default.WifiOff else Icons.Default.Error,
                    onClick = { events.onErrorClick(state) }
                )
            }

            is BillListState.Success -> {
                val bills = state.bills
                val isFiltered = (events.getCurrentFilters() != BillFilterState())


                if (bills.isEmpty()) {
                    EmptyBillsScreen(
                        modifier = modifier.verticalScroll(rememberScrollState()),
                        onRefresh = if(isFiltered) {
                            events.onEmptyFilterClick
                        } else events.onEmptyClick,
                        isFiltered = isFiltered
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
