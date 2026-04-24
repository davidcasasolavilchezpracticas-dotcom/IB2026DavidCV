package com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.horizontalpage

import androidx.compose.foundation.pager.PagerState
import com.google.firebase.analytics.FirebaseAnalytics
import com.iberdrola.practicas2026.davidcv.ui.screens.billlist.BillListState

data class HorizontalPageState(
    val lightBillsState: BillListState,
    val gasBillsState: BillListState,
    val isLightActive: Boolean,
    val isGasActive: Boolean,
    val pagerState: PagerState,
    val analytics: FirebaseAnalytics,
)
