package com.iberdrola.practicas2026.davidcv.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.iberdrola.practicas2026.davidcv.domain.model.bill.BillType
import com.iberdrola.practicas2026.davidcv.domain.model.bill.PaymentStatus
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

/**
 * BillEntity
 * Modelo de datos para la base de datos
 */
@Entity(tableName = "bills")
data class BillEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val value: Float,
    val type: BillType,
    val endDate: LocalDateTime,
    val startDate: LocalDateTime,
    val emisionDate: LocalDateTime,
    val paymentStatus: PaymentStatus,
)
