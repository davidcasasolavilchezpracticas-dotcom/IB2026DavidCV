package com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.di.DataSourceConfig
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.screens.billlist.BillListContent
import com.iberdrola.practicas2026.davidcv.ui.screens.billlist.BillListState
import com.iberdrola.practicas2026.davidcv.ui.theme.DividerGray
import kotlinx.coroutines.launch

@Composable
fun HorizontalPage(
    lightBillsState: BillListState,
    gasBillsState: BillListState,
    navController: NavController,
    pagerState: PagerState,
    isLightActive: Boolean,
    isGasActive: Boolean,
    modifier: Modifier,
    analytics: FirebaseAnalytics,
) {
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = modifier
            .zIndex(1f)
            .padding(bottom = LocalSpacing.current.xxs, start = LocalSpacing.current.lg)
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

            Spacer(modifier = modifier.width(36.dp))
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

    Spacer(modifier = modifier.height(4.dp))

    HorizontalDivider(
        color = DividerGray,
        thickness = 2.dp,
        modifier = modifier.fillMaxWidth()
    )

    Spacer(modifier = modifier.height(8.dp))

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