package com.iberdrola.practicas2026.davidcv.data.local.converters

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateConverter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? = value?.let { LocalDateTime.parse(it, formatter) }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? = date?.format(formatter)
}
