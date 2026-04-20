package com.iberdrola.practicas2026.davidcv.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.iberdrola.practicas2026.davidcv.domain.model.bill.BillType
import com.iberdrola.practicas2026.davidcv.domain.model.bill.PaymentStatus
import java.time.LocalDateTime

/**
 * BillEntity
 * Modelo de datos para la base de datos y respuesta de red
 */
@Entity(tableName = "bills")
data class BillEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    val value: Float? = null,
    val type: BillType? = null,
    val endDate: LocalDateTime? = null,
    val startDate: LocalDateTime? = null,
    val emisionDate: LocalDateTime? = null,
    val paymentStatus: PaymentStatus? = null,
)
