package com.nowiczenkoandrzej.imagecropper.core.data.local_data_source.converters

import androidx.room.TypeConverter
import java.time.LocalDate
import java.util.*

class DateConverter {

    @TypeConverter
    fun toDate(date: String): LocalDate {
        return LocalDate.parse(date)
    }

    @TypeConverter
    fun fromDate(date: LocalDate): String {
        return date.toString()
    }
}