package com.iberdrola.practicas2026.davidcv.ui.screens.billlist

import com.iberdrola.practicas2026.davidcv.ui.screens.billfilter.BillFilterState

data class BillListEvents(
    val getCurrentFilters: () -> BillFilterState,
    val onErrorClick: (BillListState) -> Unit,
    val onEmptyFilterClick: () -> Unit,
    val onFilterClick: () -> Unit,
    val onEmptyClick: () -> Unit,
    val onRefresh: () -> Unit,
)
