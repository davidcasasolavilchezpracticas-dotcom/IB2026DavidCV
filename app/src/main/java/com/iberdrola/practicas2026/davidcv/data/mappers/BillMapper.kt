package com.iberdrola.practicas2026.davidcv.data.mappers

import com.iberdrola.practicas2026.davidcv.data.local.entity.BillEntity
import com.iberdrola.practicas2026.davidcv.domain.exception.BillException
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
 * Mapper para pasar del modelo de data al modelo de domain.
 * Lanza BillException.DataCorrupted si faltan campos obligatorios.
 */
fun BillEntity.toModel(): Bill {
    return Bill(
        id = this.id ?: throw BillException.DataCorrupted,
        type = this.type ?: throw BillException.DataCorrupted,
        value = this.value ?: throw BillException.DataCorrupted,
        endDate = this.endDate ?: throw BillException.DataCorrupted,
        startDate = this.startDate ?: throw BillException.DataCorrupted,
        emisionDate = this.emisionDate ?: throw BillException.DataCorrupted,
        paymentStatus = this.paymentStatus ?: throw BillException.DataCorrupted
    )
}
