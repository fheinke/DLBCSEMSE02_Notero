package com.fheinke.notero.data.local.converter

import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateConverter {
    @TypeConverter
    fun fromEpochDay(value: Long?): LocalDate? {
        return value?.let(LocalDate::ofEpochDay)
    }

    @TypeConverter
    fun toEpochDay(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }
}
