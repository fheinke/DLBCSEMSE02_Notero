package com.fheinke.notero.domain.repository

import com.fheinke.notero.data.local.entities.JournalEntryEntity
import kotlinx.coroutines.flow.Flow

interface JournalRepository {
    fun observeEntries(): Flow<List<JournalEntryEntity>>
    suspend fun getEntryById(id: Long): JournalEntryEntity?
    suspend fun saveEntry(entry: JournalEntryEntity)
    suspend fun deleteEntry(id: Long)
}
