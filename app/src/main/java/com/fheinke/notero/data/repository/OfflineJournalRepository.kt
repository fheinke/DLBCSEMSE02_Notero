package com.fheinke.notero.data.repository

import com.fheinke.notero.data.local.dao.AttachmentDao
import com.fheinke.notero.data.local.dao.JournalEntryDao
import com.fheinke.notero.data.local.dao.PeriodRecordDao
import com.fheinke.notero.data.local.entities.JournalEntryEntity
import com.fheinke.notero.data.local.relation.JournalEntryWithAttachments
import kotlinx.coroutines.flow.Flow

class OfflineJournalRepository(
    private val journalEntryDao: JournalEntryDao,
    private val attachmentDao: AttachmentDao
) : JournalRepository {
    override fun observeAll(): Flow<List<JournalEntryEntity>> {
        return journalEntryDao.observeAll()
    }

    override suspend fun getById(id: Long): JournalEntryEntity? {
        return journalEntryDao.getById(id)
    }

    override suspend fun getWithAttachments(id: Long): JournalEntryWithAttachments? {
        return journalEntryDao.getWithAttachments(id)
    }

    override suspend fun saveEntry(entry: JournalEntryEntity) {
        journalEntryDao.upsert(entry)
    }

    override suspend fun deleteEntry(entry: JournalEntryEntity) {
        journalEntryDao.deleteEntry(entry)
    }

    override suspend fun deleteEntryById(id: Long) {
        journalEntryDao.deleteEntryById(id)
    }
}