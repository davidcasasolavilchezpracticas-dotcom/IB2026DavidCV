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
import androidx.compose.runtime.DisposableEffect
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
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.BillListHeader
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.horizontalpage.HorizontalPage
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.horizontalpage.HorizontalPageState
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.horizontalpage.useLocal
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.screens.billfilter.BillFilterState
import com.iberdrola.practicas2026.davidcv.ui.theme.EnergyGreen
import com.iberdrola.practicas2026.davidcv.ui.theme.White

/**
 * BillListScreen
 * Se define la pantalla que muestra el listado de facturas con soporte para deslizamiento entre tipos
 *
 * @param viewModel
 * @param navController
 * @param modifier
 * @param viewSelected
 */
@Composable
fun BillListScreen(
    viewModel: BillListViewModel = hiltViewModel(),
    navController: NavController,
    modifier: Modifier,
    viewSelected: Boolean = true,
    analytics: FirebaseAnalytics,
    remoteConfig: FirebaseRemoteConfig,
    onBack: () -> Unit
) {
    val view = LocalView.current
    val window = (view.context as Activity).window
    SideEffect {
        window.statusBarColor = White.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
    }

    LaunchedEffect(Unit) {
        analytics.logEvent("BillListScreen") {
            param("eventType", "View")
        }
    }

    // Simplificación de Remote Config: Leemos los valores una vez o usamos un estado
    val isGasActive = remember { remoteConfig.getBoolean("ContractGasAviable") }
    val isLightActive = remember { remoteConfig.getBoolean("ContractLightAviable") }

    val lightBillsState by viewModel.lightBillsState.collectAsStateWithLifecycle()
    val gasBillsState by viewModel.gasBillsState.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(
        initialPage =
            if (isLightActive && isGasActive && viewSelected) 0
            else if (isLightActive && isGasActive) 1
            else 0
        ,
        pageCount = {
            if (isLightActive && isGasActive) 2
            else 1
        }
    )

    // Observar el resultado de los filtros desde el SavedStateHandle de la navegación
    val filterResult by navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<BillFilterState>("filters_result")
        ?.observeAsState()
        ?: remember { mutableStateOf(null) }

    LaunchedEffect(filterResult) {
        filterResult?.let { filters ->
            viewModel.applyFilters(filters)
            analytics.logEvent("ApplyFilters") {
                param("eventType", "RelevantMovements")
            }
        }
    }

    BackHandler {
        onBack()
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
                useLocal(currentState)
                navController.popBackStack()
                state.analytics.logEvent("ButtonError") {
                    param("eventType", "Click")
                }
            },
            onEmptyClick = {
                navController.navigate(if (state.pagerState.currentPage == 0) Routes.LIST_LIGHT else Routes.LIST_GAS) {
                    // Al añadir esto, quitamos la pantalla actual de la pila antes de poner la nueva
                    popUpTo(navController.currentDestination?.route!!) { inclusive = true }
                }
                state.analytics.logEvent("ButtonEmpty") {
                    param("eventType", "Click")
                }
            },
            onFilterClick = {
                navController.navigate(Routes.FILTER)
                state.analytics.logEvent("ButtonFilter") {
                    param("eventType", "Click")
                }
            },
            onRefresh = {
                viewModel.refreshBills(state.pagerState)
                state.analytics.logEvent("ButtonRefresh") {
                    param("eventType", "Click")
                }
            }
        )

        HorizontalPage(
            modifier = modifier,
            events = events,
            state = state
        )
    }
}
