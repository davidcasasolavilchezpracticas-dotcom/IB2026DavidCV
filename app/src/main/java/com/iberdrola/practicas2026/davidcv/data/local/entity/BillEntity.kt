package com.iberdrola.practicas2026.davidcv.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.iberdrola.practicas2026.davidcv.domain.model.BillType
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

    val type: BillType,
    val value: Int,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val paymentStatus: Boolean,
)
