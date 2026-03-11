package com.iberdrola.practicas2026.davidcv.data.local.converters

import androidx.room.TypeConverter
import com.iberdrola.practicas2026.davidcv.domain.model.BillType

class BillTypeConverter {

    @TypeConverter
    fun fromBillType(type: BillType): String { return type.name }

    @TypeConverter
    fun toBillType(value: String): BillType { return BillType.valueOf(value) }
}