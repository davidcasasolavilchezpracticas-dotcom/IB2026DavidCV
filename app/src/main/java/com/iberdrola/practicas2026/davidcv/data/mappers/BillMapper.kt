package com.iberdrola.practicas2026.davidcv.data.mappers

import com.iberdrola.practicas2026.davidcv.data.local.entity.BillEntity
import com.iberdrola.practicas2026.davidcv.domain.model.Bill


fun Bill.toEntity(): BillEntity {
    return BillEntity(
        id = this.id,
        type = this.type,
        value = this.value,
        startDate = this.startDate,
        endDate = this.endDate,
        paymentStatus = this.paymentStatus
    )
}

fun BillEntity.toModel(): Bill {
    return Bill(
        id = this.id,
        type = this.type,
        value = this.value,
        startDate = this.startDate,
        endDate = this.endDate,
        paymentStatus = this.paymentStatus
    )
}
