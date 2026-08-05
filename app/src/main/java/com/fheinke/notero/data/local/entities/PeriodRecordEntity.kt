package com.fheinke.notero.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

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
