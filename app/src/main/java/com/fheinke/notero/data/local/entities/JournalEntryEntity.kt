package com.fheinke.notero.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntryEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val entryDate: Long,
    val createdAt: Long,
    val updatedAt: Long,
    val title: String,
    val text: String,
    val mood: Int?,
    val sleepScore: Int?,
    val stressLevel: Int?
    )
