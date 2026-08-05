package com.fheinke.notero.data.local.relation

import com.fheinke.notero.data.local.entities.AttachmentEntity
import androidx.room.Embedded
import androidx.room.Relation

data class JournalEntryWithAttachments (
    @Embedded
    val entry: com.fheinke.notero.data.local.entities.JournalEntryEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "journalEntryId"
    )
    val attachments: List<AttachmentEntity>
)
