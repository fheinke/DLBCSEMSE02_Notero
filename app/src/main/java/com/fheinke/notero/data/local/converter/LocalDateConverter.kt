package com.fheinke.notero.data.local.converter

import androidx.room.TypeConverter
import java.time.LocalDate

/**
 * Converter class for LocalDate to Long and vice versa.
 * This is used by Room to store LocalDate as a Long in the database.
 *
 * The LocalDate is converted to the number of days since the epoch (1970-01-01) for storage.
 *
 * @see LocalDate
 */
class LocalDateConverter {
    /**
     * Converts a Long value representing the number of days since the epoch to a LocalDate.
     *
     * @param value The Long value to convert.
     * @return The corresponding LocalDate, or null if the input is null.
     */
    @TypeConverter
    fun fromEpochDay(value: Long?): LocalDate? {
        return value?.let(LocalDate::ofEpochDay)
    }

    /**
     * Converts a LocalDate to a Long value representing the number of days since the epoch.
     *
     * @param date The LocalDate to convert.
     * @return The corresponding Long value, or null if the input is null.
     */
    @TypeConverter
    fun toEpochDay(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }
}
