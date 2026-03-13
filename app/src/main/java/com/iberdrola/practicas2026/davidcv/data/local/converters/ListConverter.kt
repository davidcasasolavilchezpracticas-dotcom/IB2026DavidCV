package com.iberdrola.practicas2026.davidcv.data.local.converters

import androidx.room.TypeConverter

/**
 * ListConverter
 * Convertidor para la base de datos
 */
class ListConverter {
    @TypeConverter
    fun fromList(list: List<String>?): String? {
        return list?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toList(data: String?): List<String>? {
        return data?.split(",")?.map { it.trim() }
    }
}
