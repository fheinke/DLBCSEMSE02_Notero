package com.fheinke.notero.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "journal_entries")
data class JournalEntryEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val entryDate: LocalDate,
    val createdAt: Long,
    val updatedAt: Long,
    val title: String,
    val text: String,
    val mood: Int?,
    val sleepScore: Int?,
    val stressLevel: Int?
    )
