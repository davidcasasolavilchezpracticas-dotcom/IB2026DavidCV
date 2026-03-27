package com.iberdrola.practicas2026.davidcv.ui.screens.billlist

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.di.DataSourceConfig
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.BillListHeader
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.TabItem
import com.iberdrola.practicas2026.davidcv.ui.base.screens.LoadingScreen
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.screens.billfilter.BillFilterState
import com.iberdrola.practicas2026.davidcv.ui.theme.DividerGray
import kotlinx.coroutines.launch

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
    remoteConfig: FirebaseRemoteConfig
) {
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
    val coroutineScope = rememberCoroutineScope()

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
        navController.navigate(Routes.BACK)
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
            .padding(LocalSpacing.current.lg)
    ) {
        BillListHeader(
            modifier = modifier,
        )

        Row(
            modifier = modifier
                .zIndex(1f)
                .padding(bottom = LocalSpacing.current.xxs)
                .fillMaxWidth()
        ) {
            if (isLightActive) {
                TabItem(
                    text = stringResource(R.string.blsTabText1),
                    isSelected = pagerState.currentPage == 0,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                        analytics.logEvent("SlideToGas") {
                            param("eventType", "RelevantMovements")
                        }
                    }
                )

                Spacer(modifier = modifier.width(24.dp))
            }

            if (isGasActive) {
                TabItem(
                    text = stringResource(R.string.blsTabText2),
                    isSelected = pagerState.currentPage == 1,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                        analytics.logEvent("SlideToLight") {
                            param("eventType", "RelevantMovements")
                        }
                    }
                )
            }
        }

        Divider(
            color = DividerGray,
            thickness = 2.dp,
        )

        Spacer(modifier = modifier.height(24.dp))

        HorizontalPager(
            state = pagerState,
            modifier = modifier.fillMaxSize()
        ) { page ->
            val currentState = when {
                isLightActive && isGasActive -> if (page == 0) lightBillsState else gasBillsState
                isLightActive -> lightBillsState
                isGasActive -> gasBillsState
                else -> BillListState.Success(emptyList())
            }

            BillListContent(
                state = currentState,
                modifier = modifier,
                onErrorClick = {
                    DataSourceConfig.useNetwork = !DataSourceConfig.useNetwork
                    navController.popBackStack()
                    analytics.logEvent("ButtonError") {
                        param("eventType", "Click")
                    }
                },
                onEmptyClick = {
                    navController.navigate(if (pagerState.currentPage == 0) Routes.LIST_LIGHT else Routes.LIST_GAS) {
                        // Al añadir esto, quitamos la pantalla actual de la pila antes de poner la nueva
                        popUpTo(navController.currentDestination?.route!!) { inclusive = true }
                    }
                    analytics.logEvent("ButtonEmpty") {
                        param("eventType", "Click")
                    }
                },
                onFilterClick = {
                    navController.navigate(Routes.FILTER)
                    analytics.logEvent("ButtonFilter") {
                        param("eventType", "Click")
                    }
                }
            )
        }
    }
}

