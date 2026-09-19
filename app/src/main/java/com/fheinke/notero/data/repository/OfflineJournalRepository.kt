package com.fheinke.notero.data.repository

import com.fheinke.notero.data.local.dao.JournalEntryDao
import com.fheinke.notero.data.local.entities.JournalEntryEntity
import kotlinx.coroutines.flow.Flow

/**
 * OfflineJournalRepository is an implementation of the JournalRepository interface that interacts with the local database.
 * It uses DAOs (Data Access Objects) to perform CRUD operations on journal entries.
 *
 * @property journalEntryDao The DAO for accessing journal entries in the local database.
 */
class OfflineJournalRepository(
    private val journalEntryDao: JournalEntryDao,
) : JournalRepository {

    /**
     * Retrieves all journal entries as a Flow of a list of JournalEntryEntity.
     *
     * @return A Flow emitting a list of all journal entries.
     */
    override fun getAll(): Flow<List<JournalEntryEntity>> {
        return journalEntryDao.getAll()
    }

    /**
     * Retrieves a journal entry by its unique identifier.
     *
     * @param id The unique identifier of the journal entry.
     * @return The JournalEntryEntity corresponding to the provided id, or null if not found.
     */
    override suspend fun getById(id: Long): JournalEntryEntity? {
        return journalEntryDao.getById(id)
    }

    /**
     * Saves a journal entry to the data source.
     *
     * @param entry The JournalEntryEntity to be saved.
     */
    override suspend fun saveEntry(entry: JournalEntryEntity) {
        journalEntryDao.upsert(entry)
    }

    /**
     * Deletes a journal entry from the data source.
     *
     * @param entry The JournalEntryEntity to be deleted.
     */
    override suspend fun deleteEntry(entry: JournalEntryEntity) {
        journalEntryDao.deleteEntry(entry)
    }

    /**
     * Deletes a journal entry by its unique identifier.
     *
     * @param id The unique identifier of the journal entry to be deleted.
     */
    override suspend fun deleteEntryById(id: Long) {
        journalEntryDao.deleteEntryById(id)
    }
}