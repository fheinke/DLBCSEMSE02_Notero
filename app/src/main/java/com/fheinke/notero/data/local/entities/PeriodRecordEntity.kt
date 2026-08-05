package com.fheinke.notero.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "period_records")
data class PeriodRecordEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val createdAt: Long,
    val updatedAt: Long,
    val startDate: Long,
    val endDate: Long?,
    val flowIntensity: Int?,
    @ColumnInfo(defaultValue = "")
    val note: String?
    )
