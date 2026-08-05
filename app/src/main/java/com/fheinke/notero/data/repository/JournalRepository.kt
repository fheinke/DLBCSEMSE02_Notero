package com.fheinke.notero.data.repository

import com.fheinke.notero.data.local.entities.JournalEntryEntity
import com.fheinke.notero.data.local.relation.JournalEntryWithAttachments
import kotlinx.coroutines.flow.Flow

interface JournalRepository {
    fun observeAll(): Flow<List<JournalEntryEntity>>
    suspend fun getById(id: Long): JournalEntryEntity?
    suspend fun getWithAttachments(id: Long): JournalEntryWithAttachments?
    suspend fun saveEntry(entry: JournalEntryEntity)
    suspend fun deleteEntry(entry: JournalEntryEntity)
    suspend fun deleteEntryById(id: Long)
}