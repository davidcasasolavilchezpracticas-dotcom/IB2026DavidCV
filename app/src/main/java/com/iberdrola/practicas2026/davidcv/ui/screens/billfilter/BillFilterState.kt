package com.iberdrola.practicas2026.davidcv.ui.screens.billfilter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.time.LocalDateTime

@Parcelize
data class BillFilterState(
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
    val priceRange: @RawValue ClosedFloatingPointRange<Float>? = null,
    val paymentStatusPaid: Boolean = false,
    val paymentStatusPending: Boolean = false,
    val paymentStatusTramited: Boolean = false,
    val paymentStatusCanceled: Boolean = false,
    val paymentStatusFixed: Boolean = false,
) : Parcelable