package com.iberdrola.practicas2026.davidcv.domain.model.bill

import java.time.LocalDateTime

data class Bill(
    val id: Int,
    val value: Float,
    val type: BillType,
    val endDate: LocalDateTime,
    val startDate: LocalDateTime,
    val emisionDate: LocalDateTime,
    val paymentStatus: PaymentStatus,
)