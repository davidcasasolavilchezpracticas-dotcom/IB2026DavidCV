package com.iberdrola.practicas2026.davidcv.data.mappers

import com.iberdrola.practicas2026.davidcv.data.local.entity.BillEntity
import com.iberdrola.practicas2026.davidcv.domain.model.Bill

/**
 * Mapper para pasar del modelo de domain al modelo de data
 */
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

/**
 * Mapper para pasar del modelo de data al modelo de domain
 */
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
