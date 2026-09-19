package com.fheinke.notero.data.local.dao

import com.fheinke.notero.data.local.entities.PeriodRecordEntity
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the PeriodRecordEntity.
 * Provides methods for interacting with the period_records table in the database.
 */
@Dao
interface PeriodRecordDao {
    /**
     * Retrieves all period records from the database, ordered by start date in descending order.
     *
     * @return A Flow emitting a list of PeriodRecordEntity objects.
     */
    @Query("SELECT * FROM period_records ORDER BY startDate DESC")
    fun getAll(): Flow<List<PeriodRecordEntity>>

    /**
     * Retrieves a specific period record by its ID.
     *
     * @param id The ID of the period record to retrieve.
     * @return The PeriodRecordEntity with the specified ID, or null if not found.
     */
    @Query("SELECT * FROM period_records WHERE id = :id")
    suspend fun getById(id: Long): PeriodRecordEntity?

    /**
     * Inserts or updates a period record in the database.
     *
     * @param record The PeriodRecordEntity to insert or update.
     */
    @Upsert
    suspend fun upsert(record: PeriodRecordEntity)

    /**
     * Deletes a specific period record from the database.
     *
     * @param record The PeriodRecordEntity to delete.
     */
    @Delete
    suspend fun delete(record: PeriodRecordEntity)

    /**
     * Deletes a period record from the database by its ID.
     *
     * @param id The ID of the period record to delete.
     */
    @Query("DELETE FROM period_records WHERE id = :id")
    suspend fun deleteById(id: Long)
}
