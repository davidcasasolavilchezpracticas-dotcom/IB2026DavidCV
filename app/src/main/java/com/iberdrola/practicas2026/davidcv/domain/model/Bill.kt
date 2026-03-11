package com.iberdrola.practicas2026.davidcv.domain.model

import java.time.LocalDateTime

data class Bill(
    val id: Int,
    val type: BillType,
    val value: Int,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val paymentStatus: Boolean,
)
