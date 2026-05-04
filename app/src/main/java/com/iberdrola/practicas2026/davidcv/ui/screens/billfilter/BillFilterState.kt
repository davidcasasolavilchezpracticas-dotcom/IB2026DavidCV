package com.iberdrola.practicas2026.davidcv.ui.screens.billfilter

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Parceler manual para LocalDateTime ya que no es Parcelable por defecto.
 */
object LocalDateTimeParceler : Parceler<LocalDateTime?> {
    override fun create(parcel: Parcel): LocalDateTime? {
        val millis = parcel.readLong()
        return if (millis != -1L) {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault())
        } else null
    }

    override fun LocalDateTime?.write(parcel: Parcel, flags: Int) {
        parcel.writeLong(this?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli() ?: -1L)
    }
}

/**
 * Parceler para ClosedFloatingPointRange<Float>
 */
object FloatRangeParceler : Parceler<ClosedFloatingPointRange<Float>?> {
    override fun create(parcel: Parcel): ClosedFloatingPointRange<Float>? {
        return if (parcel.readInt() == 1) parcel.readFloat()..parcel.readFloat() else null
    }

    override fun ClosedFloatingPointRange<Float>?.write(parcel: Parcel, flags: Int) {
        if (this != null) {
            parcel.writeInt(1)
            parcel.writeFloat(start)
            parcel.writeFloat(endInclusive)
        } else {
            parcel.writeInt(0)
        }
    }
}

@Parcelize
data class BillFilterState(
    @kotlinx.parcelize.TypeParceler<LocalDateTime?, LocalDateTimeParceler>
    val startDate: LocalDateTime? = null,
    @kotlinx.parcelize.TypeParceler<LocalDateTime?, LocalDateTimeParceler>
    val endDate: LocalDateTime? = null,
    @kotlinx.parcelize.TypeParceler<ClosedFloatingPointRange<Float>?, FloatRangeParceler>
    val priceRange: @RawValue ClosedFloatingPointRange<Float>? = null,
    val paymentStatusPaid: Boolean = false,
    val paymentStatusPending: Boolean = false,
    val paymentStatusTramited: Boolean = false,
    val paymentStatusCanceled: Boolean = false,
    val paymentStatusFixed: Boolean = false,
) : Parcelable
