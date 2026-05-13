package com.iberdrola.practicas2026.davidcv.ui.screens.billlist

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.iberdrola.practicas2026.davidcv.ui.base.common.ClickEventManager
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.BillListHeader
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.horizontalpage.HorizontalPage
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.horizontalpage.HorizontalPageState
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.horizontalpage.useLocal
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.screens.billfilter.BillFilterState
import com.iberdrola.practicas2026.davidcv.ui.theme.White

/**
 * BillListScreen
 * Pantalla que muestra el listado de facturas con soporte para deslizamiento entre tipos
 */
@Composable
fun BillListScreen(
    viewModel: BillListViewModel = hiltViewModel(),
    onNavigatePopUpTo: (String, String) -> Unit,
    remoteConfig: FirebaseRemoteConfig,
    navController: NavController,
    viewSelected: Boolean = true,
    analytics: FirebaseAnalytics,
    onNavigate: (String) -> Unit,
    manager: ClickEventManager,
    modifier: Modifier,
    isClosing: Boolean,
    onBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        analytics.logEvent("BillListScreen") {
            param("eventType", "View")
        }
    }

    val isLightActive = remember { remoteConfig.getBoolean("ContractLightAviable") }
    val isGasActive = remember { remoteConfig.getBoolean("ContractGasAviable") }

    val lightBillsState by viewModel.lightBillsState.collectAsStateWithLifecycle()
    val gasBillsState by viewModel.gasBillsState.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(
        initialPage = if (isLightActive && isGasActive && viewSelected) 0
                     else if (isLightActive && isGasActive) 1
                     else 0,
        pageCount = { if (isLightActive && isGasActive) 2 else 1 }
    )

    // Observar el resultado de los filtros
    val filterResult by navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<BillFilterState>("filters_result")
        ?.observeAsState()
        ?: remember { mutableStateOf(null) }


    LaunchedEffect(filterResult) {
        filterResult?.let { filters ->
            viewModel.applyFilters(filters)
            navController.currentBackStackEntry?.savedStateHandle?.remove<BillFilterState>("filters_result")
            analytics.logEvent("ApplyFilters") {
                param("eventType", "RelevantMovements")
            }
        }
    }

    BackHandler {
        onBack()
        navController.currentBackStackEntry?.savedStateHandle?.remove<BillFilterState>("initial_filters")
        analytics.logEvent("ButtonBack") {
            param("eventType", "RelevantMovements")
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getLightBills()
        viewModel.getGasBills()
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        BillListHeader(
            modifier = modifier.padding(start = LocalSpacing.current.lg),
        )

        Spacer(modifier = modifier.height(4.dp))

        val state = HorizontalPageState(
            lightBillsState = lightBillsState,
            gasBillsState = gasBillsState,
            isLightActive = isLightActive,
            isGasActive = isGasActive,
            pagerState = pagerState,
            analytics = analytics
        )

        val events = BillListEvents(
            onErrorClick = { currentState ->
                viewModel.onErrorClick(
                    navController = navController,
                    currentState = currentState,
                    useLocal = { useLocal(it) }
                )
                state.analytics.logEvent("ButtonError") {
                    param("eventType", "Click")
                }
            },
            onEmptyClick = {
                onNavigatePopUpTo(Routes.INITIAL, navController.currentDestination?.route!!)
                state.analytics.logEvent("ButtonEmpty") {
                    param("eventType", "Click")
                }
            },
            onEmptyFilterClick = {
                navController.currentBackStackEntry?.savedStateHandle?.apply {
                    remove<BillFilterState>("filters_result")
                    remove<BillFilterState>("initial_filters")
                }
                viewModel.applyFilters(BillFilterState())
                viewModel.refreshBills(state.pagerState)
                state.analytics.logEvent("ButtonEmpty") {
                    param("eventType", "Click")
                }
            },
            onFilterClick = {
                val currentFilters = viewModel.getCurrentFilters()
                val priceLimits = viewModel.getPriceLimits()
                val dateLimits = viewModel.getDateLimits()
                
                navController.currentBackStackEntry?.savedStateHandle?.apply {
                    set("initial_filters", currentFilters)
                    priceLimits?.let {
                        set("min_limit", it.first)
                        set("max_limit", it.second)
                    }
                    dateLimits?.let {
                        set("min_date_limit", it.first.toString())
                        set("max_date_limit", it.second.toString())
                    }
                }

                onNavigate(Routes.FILTER)
                state.analytics.logEvent("ButtonFilter") {
                    param("eventType", "Click")
                }
            },
            onDeleteFilters = {
                // Limpiamos rastro en navegación y ViewModel
                navController.currentBackStackEntry?.savedStateHandle?.apply {
                    remove<BillFilterState>("filters_result")
                    remove<BillFilterState>("initial_filters")
                }
                viewModel.onDeleteFilters()
                viewModel.refreshBills(state.pagerState)
                state.analytics.logEvent("ButtonDeleteFilters") {
                    param("eventType", "Click")
                }
            },
            onRefresh = {
                viewModel.refreshBills(state.pagerState)
                state.analytics.logEvent("ButtonRefresh") {
                    param("eventType", "Click")
                }
            },
            getCurrentFilters = viewModel::getCurrentFilters,
            getSelectedFilters = viewModel::filtersActives,
            getPriceLimits = viewModel::getPriceLimits
        )

        HorizontalPage(
            enabled = !isClosing,
            modifier = modifier,
            manager = manager,
            events = events,
            state = state,
        )
    }
}
