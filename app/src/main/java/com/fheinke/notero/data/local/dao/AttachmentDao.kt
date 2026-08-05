package com.fheinke.notero.data.local.dao

import com.fheinke.notero.data.local.entities.AttachmentEntity
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AttachmentDao {
    @Query("SELECT * FROM attachments WHERE journalEntryId = :journalEntryId")
    suspend fun getByJournalEntryId(journalEntryId: Long): Flow<List<AttachmentEntity>>

    @Upsert
    suspend fun upsert(attachment: AttachmentEntity)

    @Delete
    suspend fun delete(attachment: AttachmentEntity)

    @Query("DELETE FROM attachments WHERE id = :id")
    suspend fun deleteById(id: Long)
}
