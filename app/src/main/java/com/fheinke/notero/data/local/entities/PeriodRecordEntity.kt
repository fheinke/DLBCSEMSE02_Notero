package com.fheinke.notero.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * Represents a period record entity in the database.
 *
 * @constructor Creates a new instance of PeriodRecordEntity.
 *
 * @property id The unique identifier for the period record.
 * @property createdAt The timestamp when the record was created.
 * @property updatedAt The timestamp when the record was last updated.
 * @property startDate The start date of the period.
 * @property endDate The end date of the period (nullable).
 * @property flowIntensity The intensity of the flow (nullable).
 * @property note An optional note associated with the period record (nullable).
 */
@Entity(tableName = "period_records")
data class PeriodRecordEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val createdAt: Long,
    val updatedAt: Long,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val flowIntensity: Int?,
    val note: String? = null
    )
