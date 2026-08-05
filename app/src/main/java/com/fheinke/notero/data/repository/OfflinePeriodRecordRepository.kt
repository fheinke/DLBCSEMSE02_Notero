package com.fheinke.notero.data.repository

import com.fheinke.notero.data.local.dao.PeriodRecordDao
import com.fheinke.notero.data.local.entities.PeriodRecordEntity
import kotlinx.coroutines.flow.Flow

class OfflinePeriodRecordRepository(
    private val periodRecordDao: PeriodRecordDao
) : PeriodRecordRepository {
    override fun observeAll(): Flow<List<PeriodRecordEntity>> {
        return periodRecordDao.observeAll()
    }

    override suspend fun getById(id: Long): PeriodRecordEntity? {
        return periodRecordDao.getById(id)
    }

    override suspend fun saveEntry(entry: PeriodRecordEntity) {
        periodRecordDao.upsert(entry)
    }

    override suspend fun deleteEntry(entry: PeriodRecordEntity) {
        periodRecordDao.delete(entry)
    }

    override suspend fun deleteEntryById(id: Long) {
        periodRecordDao.deleteById(id)
    }
}