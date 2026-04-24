package com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.horizontalpage

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.di.DataSourceConfig
import com.iberdrola.practicas2026.davidcv.domain.exception.BillException
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.TabItem
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.screens.billlist.BillListContent
import com.iberdrola.practicas2026.davidcv.ui.screens.billlist.BillListEvents
import com.iberdrola.practicas2026.davidcv.ui.screens.billlist.BillListState
import com.iberdrola.practicas2026.davidcv.ui.theme.DividerGray
import kotlinx.coroutines.launch

@Composable
fun HorizontalPage(
    state: HorizontalPageState,
    events: BillListEvents,
    modifier: Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = modifier
            .zIndex(1f)
            .padding(bottom = LocalSpacing.current.xxs, start = LocalSpacing.current.lg)
            .fillMaxWidth()
    ) {
        if (state.isLightActive) {
            TabItem(
                text = stringResource(R.string.blsTabText1),
                isSelected = state.pagerState.currentPage == 0,
                onClick = {
                    coroutineScope.launch {
                        state.pagerState.animateScrollToPage(0)
                    }
                    state.analytics.logEvent("SlideToGas") {
                        param("eventType", "RelevantMovements")
                    }
                }
            )

            Spacer(modifier = modifier.width(36.dp))
        }

        if (state.isGasActive) {
            TabItem(
                text = stringResource(R.string.blsTabText2),
                isSelected = state.pagerState.currentPage == 1,
                onClick = {
                    coroutineScope.launch {
                        state.pagerState.animateScrollToPage(1)
                    }
                    state.analytics.logEvent("SlideToLight") {
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

    Spacer(modifier = modifier.height(4.dp))

    HorizontalPager(
        state = state.pagerState,
        modifier = modifier.fillMaxSize()
    ) { page ->
        val currentState = when {
            state.isLightActive && state.isGasActive -> if (page == 0) state.lightBillsState else state.gasBillsState
            state.isLightActive -> state.lightBillsState
            state.isGasActive -> state.gasBillsState
            else -> BillListState.Success(emptyList())
        }

        BillListContent(
            state = currentState,
            modifier = modifier,
            events = events
        )
    }
}

fun useLocal(currentState: BillListState) {
    if (currentState is BillListState.Error && currentState.exception == BillException.ConexionFailed){
        DataSourceConfig.useNetwork = false
    }
}