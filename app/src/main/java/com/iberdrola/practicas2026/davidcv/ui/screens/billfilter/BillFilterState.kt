package com.iberdrola.practicas2026.davidcv.ui.screens.billfilter

import java.time.LocalDateTime

data class BillFilterState(
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
    val priceRange: ClosedFloatingPointRange<Float>? = null,
    val paymentStatusPaid: Boolean = false,
    val paymentStatusPending: Boolean = false,
    val paymentStatusTramited: Boolean = false,
    val paymentStatusCanceled: Boolean = false,
    val paymentStatusFixed: Boolean = false,
)