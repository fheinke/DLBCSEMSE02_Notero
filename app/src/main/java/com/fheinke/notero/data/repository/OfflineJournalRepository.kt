package com.fheinke.notero.data.repository

import com.fheinke.notero.data.local.dao.JournalEntryDao
import com.fheinke.notero.data.local.entities.JournalEntryEntity
import com.fheinke.notero.domain.repository.JournalRepository
import kotlinx.coroutines.flow.Flow

class OfflineJournalRepository(
    private val journalEntryDao: JournalEntryDao
) : JournalRepository {
    override fun observeEntries(): Flow<List<JournalEntryEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getEntryById(id: Long): JournalEntryEntity? {
        TODO("Not yet implemented")
    }

    override suspend fun saveEntry(entry: JournalEntryEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteEntry(id: Long) {
        TODO("Not yet implemented")
    }

}