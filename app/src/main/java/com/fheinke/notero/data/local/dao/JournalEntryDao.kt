package com.fheinke.notero.data.local.dao

import com.fheinke.notero.data.local.entities.JournalEntryEntity
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the JournalEntryEntity.
 * This interface defines methods for accessing and manipulating journal entries in the database.
 *
 * The methods include:
 * - Retrieving all journal entries ordered by entry date in descending order.
 * - Retrieving a specific journal entry by its ID.
 * - Inserting or updating a journal entry.
 * - Deleting a specific journal entry.
 * - Deleting a journal entry by its ID.
 *
 * The DAO uses Kotlin coroutines and Flow for asynchronous operations.
 *
 * @see JournalEntryEntity
 */
@Dao
interface JournalEntryDao {
    /**
     * Retrieves all journal entries from the database, ordered by entry date in descending order.
     *
     * @return A Flow emitting a list of JournalEntryEntity objects.
     */
    @Query("SELECT * FROM journal_entries ORDER BY entryDate DESC")
    fun getAll(): Flow<List<JournalEntryEntity>>

    /**
     * Retrieves a specific journal entry by its ID.
     *
     * @param id The ID of the journal entry to retrieve.
     * @return The JournalEntryEntity with the specified ID, or null if not found.
     */
    @Query("SELECT * FROM journal_entries WHERE id = :id")
    suspend fun getById(id: Long): JournalEntryEntity?

    /**
     * Inserts or updates a journal entry in the database.
     *
     * @param entry The JournalEntryEntity to insert or update.
     */
    @Upsert
    suspend fun upsert(entry: JournalEntryEntity)

    /**
     * Deletes a specific journal entry from the database.
     *
     * @param entry The JournalEntryEntity to delete.
     */
    @Delete
    suspend fun deleteEntry(entry: JournalEntryEntity)

    /**
     * Deletes a journal entry from the database by its ID.
     *
     * @param id The ID of the journal entry to delete.
     */
    @Query("DELETE FROM journal_entries WHERE id = :id")
    suspend fun deleteEntryById(id: Long)
}
