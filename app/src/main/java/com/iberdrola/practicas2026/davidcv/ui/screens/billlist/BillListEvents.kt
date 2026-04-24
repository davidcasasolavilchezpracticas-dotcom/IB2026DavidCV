package com.iberdrola.practicas2026.davidcv.ui.screens.billlist

data class BillListEvents(
    val onErrorClick: (BillListState) -> Unit,
    val onFilterClick: () -> Unit,
    val onEmptyClick: () -> Unit,
    val onRefresh: () -> Unit,
)
