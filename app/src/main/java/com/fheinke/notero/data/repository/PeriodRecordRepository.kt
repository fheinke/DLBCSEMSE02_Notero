package com.fheinke.notero.data.repository

import com.fheinke.notero.data.local.entities.PeriodRecordEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing period records.
 * This interface defines the contract for accessing and manipulating period record data.
 * Implementations of this interface should provide the necessary methods to retrieve, save, and delete period records.
 *
 * @see OfflinePeriodRecordRepository
 */
interface PeriodRecordRepository {

    /**
     * Retrieves all period records as a Flow of a list of PeriodRecordEntity.
     *
     * @return A Flow emitting a list of all period records.
     */
    fun getAll(): Flow<List<PeriodRecordEntity>>

    /**
     * Retrieves a period record by its unique identifier.
     *
     * @param id The unique identifier of the period record.
     * @return The PeriodRecordEntity corresponding to the provided id, or null if not found.
     */
    suspend fun getById(id: Long): PeriodRecordEntity?

    /**
     * Saves a period record to the data source.
     *
     * @param entry The PeriodRecordEntity to be saved.
     */
    suspend fun saveEntry(entry: PeriodRecordEntity)

    /**
     * Deletes a period record from the data source.
     *
     * @param entry The PeriodRecordEntity to be deleted.
     */
    suspend fun deleteEntry(entry: PeriodRecordEntity)

    /**
     * Deletes a period record by its unique identifier.
     *
     * @param id The unique identifier of the period record to be deleted.
     */
    suspend fun deleteEntryById(id: Long)
}
