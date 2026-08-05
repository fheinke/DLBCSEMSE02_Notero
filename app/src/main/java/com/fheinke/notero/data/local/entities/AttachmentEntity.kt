package com.fheinke.notero.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "attachments",
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = JournalEntryEntity::class,
            parentColumns = ["id"],
            childColumns = ["journalEntryId"],
            onDelete = androidx.room.ForeignKey.CASCADE
        )
    ],
    indices = [
        androidx.room.Index(value = ["journalEntryId"])
    ]
)
data class AttachmentEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val journalEntryId: Long,
    val uri: String,
    val mimeType: String,
    @ColumnInfo(defaultValue = "")
    val caption: String?
    )
