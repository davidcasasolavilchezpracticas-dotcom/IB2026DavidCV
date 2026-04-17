package com.iberdrola.practicas2026.davidcv.data.mappers

import com.iberdrola.practicas2026.davidcv.data.local.entity.BillEntity
import com.iberdrola.practicas2026.davidcv.domain.model.bill.Bill

/**
 * Mapper para pasar del modelo de domain al modelo de data
 */
fun Bill.toEntity(): BillEntity {
    return BillEntity(
        id = this.id,
        type = this.type,
        value = this.value,
        endDate = this.endDate,
        startDate = this.startDate,
        emisionDate = this.emisionDate,
        paymentStatus = this.paymentStatus,
    )
}

/**
 * Mapper para pasar del modelo de data al modelo de domain
 */
fun BillEntity.toModel(): Bill {
    return Bill(
        id = this.id,
        type = this.type,
        value = this.value,
        endDate = this.endDate,
        startDate = this.startDate,
        emisionDate = this.emisionDate,
        paymentStatus = this.paymentStatus
    )
}
