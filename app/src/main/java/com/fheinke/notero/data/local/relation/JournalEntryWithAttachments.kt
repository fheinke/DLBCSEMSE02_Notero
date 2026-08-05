package com.fheinke.notero.data.local.relation

import com.fheinke.notero.data.local.entities.AttachmentEntity
import com.fheinke.notero.data.local.entities.JournalEntryEntity
import androidx.room.Embedded
import androidx.room.Relation

data class JournalEntryWithAttachments (
    @Embedded
    val entry: JournalEntryEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "journalEntryId"
    )
    val attachments: List<AttachmentEntity>
)
