package com.fheinke.notero.data.repository

import com.fheinke.notero.data.local.dao.PeriodRecordDao
import com.fheinke.notero.data.local.entities.PeriodRecordEntity
import kotlinx.coroutines.flow.Flow

/**
 * OfflinePeriodRecordRepository is an implementation of the PeriodRecordRepository interface that interacts with the local database.
 * It uses DAOs (Data Access Objects) to perform CRUD operations on period records.
 *
 * @property periodRecordDao The DAO for accessing period records in the local database.
 */
class OfflinePeriodRecordRepository(
    private val periodRecordDao: PeriodRecordDao
) : PeriodRecordRepository {

    /**
     * Retrieves all period records from the local database as a Flow of a list of PeriodRecordEntity.
     *
     * @return A Flow emitting a list of all period records.
     */
    override fun getAll(): Flow<List<PeriodRecordEntity>> {
        return periodRecordDao.getAll()
    }

    /**
     * Retrieves a period record by its unique identifier.
     *
     * @param id The unique identifier of the period record.
     * @return The PeriodRecordEntity corresponding to the provided id, or null if not found.
     */
    override suspend fun getById(id: Long): PeriodRecordEntity? {
        return periodRecordDao.getById(id)
    }

    /**
     * Saves a period record to the data source.
     *
     * @param entry The PeriodRecordEntity to be saved.
     */
    override suspend fun saveEntry(entry: PeriodRecordEntity) {
        periodRecordDao.upsert(entry)
    }

    /**
     * Deletes a period record from the data source.
     *
     * @param entry The PeriodRecordEntity to be deleted.
     */
    override suspend fun deleteEntry(entry: PeriodRecordEntity) {
        periodRecordDao.delete(entry)
    }

    /**
     * Deletes a period record by its unique identifier.
     *
     * @param id The unique identifier of the period record to be deleted.
     */
    override suspend fun deleteEntryById(id: Long) {
        periodRecordDao.deleteById(id)
    }
}