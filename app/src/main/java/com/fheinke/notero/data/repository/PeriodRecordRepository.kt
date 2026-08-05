package com.fheinke.notero.data.repository

import com.fheinke.notero.data.local.entities.PeriodRecordEntity
import kotlinx.coroutines.flow.Flow

interface PeriodRecordRepository {
    fun observeAll(): Flow<List<PeriodRecordEntity>>
    suspend fun getById(id: Long): PeriodRecordEntity?
    suspend fun saveEntry(entry: PeriodRecordEntity)
    suspend fun deleteEntry(entry: PeriodRecordEntity)
    suspend fun deleteEntryById(id: Long)
}
