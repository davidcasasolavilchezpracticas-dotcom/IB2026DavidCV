package com.iberdrola.practicas2026.davidcv.data.local.converters

import androidx.room.TypeConverter
import com.iberdrola.practicas2026.davidcv.domain.model.PaymentStatus

/**
 * PaymentStatusConverter
 * Convertidor para la base de datos
 */
class PaymentStatusConverter {

    @TypeConverter
    fun fromPaymentStatus(status: PaymentStatus): String {
        return status.name
    }

    @TypeConverter
    fun toPaymentStatus(value: String): PaymentStatus {
        return PaymentStatus.valueOf(value)
    }
}
