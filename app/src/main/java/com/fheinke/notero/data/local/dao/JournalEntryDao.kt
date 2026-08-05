package com.fheinke.notero.data.local.dao

import com.fheinke.notero.data.local.entities.JournalEntryEntity
import com.fheinke.notero.data.local.relation.JournalEntryWithAttachments
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalEntryDao {
    @Query("SELECT * FROM journal_entries ORDER BY entryDate DESC")
    fun observeAll(): Flow<List<JournalEntryEntity>>

    @Query("SELECT * FROM journal_entries WHERE id = :id")
    suspend fun getById(id: Long): JournalEntryEntity?

    @Transaction
    @Query("SELECT * FROM journal_entries WHERE id = :id")
    suspend fun getWithAttachments(
        id: Long
    ): JournalEntryWithAttachments?

    @Upsert
    suspend fun upsert(entry: JournalEntryEntity)

    @Delete
    suspend fun deleteEntry(entry: JournalEntryEntity)

    @Query("DELETE FROM journal_entries WHERE id = :id")
    suspend fun deleteEntryById(id: Long)
}
