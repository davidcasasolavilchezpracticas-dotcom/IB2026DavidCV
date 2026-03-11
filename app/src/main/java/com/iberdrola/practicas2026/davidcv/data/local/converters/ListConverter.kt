package com.iberdrola.practicas2026.davidcv.data.local.converters

import androidx.room.TypeConverter

class ListConverter {
    @TypeConverter
    fun fromList(list: List<String>?): String? {
        // Converts the List to a single String separated by commas
        return list?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toList(data: String?): List<String>? {
        // Converts the String back into a List
        return data?.split(",")?.map { it.trim() }
    }
}
