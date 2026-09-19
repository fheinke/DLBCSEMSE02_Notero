package com.fheinke.notero.data.repository

import com.fheinke.notero.data.local.entities.JournalEntryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing journal entries.
 * This interface defines the contract for accessing and manipulating journal entries in the data layer.
 *
 * Implementations of this interface should provide the necessary methods to retrieve, save, and delete journal entries.
 *
 * @see JournalEntryEntity
 */
interface JournalRepository {

    /**
     * Retrieves all journal entries as a Flow of a list of JournalEntryEntity.
     *
     * @return A Flow emitting a list of all journal entries.
     */
    fun getAll(): Flow<List<JournalEntryEntity>>

    /**
     * Retrieves a journal entry by its unique identifier.
     *
     * @param id The unique identifier of the journal entry.
     * @return The JournalEntryEntity corresponding to the provided id, or null if not found.
     */
    suspend fun getById(id: Long): JournalEntryEntity?

    /**
     * Saves a journal entry to the data source.
     *
     * @param entry The JournalEntryEntity to be saved.
     */
    suspend fun saveEntry(entry: JournalEntryEntity)

    /**
     * Deletes a journal entry from the data source.
     *
     * @param entry The JournalEntryEntity to be deleted.
     */
    suspend fun deleteEntry(entry: JournalEntryEntity)

    /**
     * Deletes a journal entry by its unique identifier.
     *
     * @param id The unique identifier of the journal entry to be deleted.
     */
    suspend fun deleteEntryById(id: Long)
}