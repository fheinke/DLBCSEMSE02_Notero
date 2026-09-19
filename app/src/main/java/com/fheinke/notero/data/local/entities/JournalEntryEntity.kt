package com.fheinke.notero.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * Represents a journal entry in the local database.
 *
 * @constructor Creates a new instance of JournalEntryEntity.
 *
 * @property id The unique identifier for the journal entry.
 * @property entryDate The date of the journal entry.
 * @property createdAt The timestamp when the journal entry was created.
 * @property updatedAt The timestamp when the journal entry was last updated.
 * @property title The title of the journal entry.
 * @property text The content of the journal entry.
 * @property mood The mood associated with the journal entry (optional).
 * @property sleepScore The sleep score associated with the journal entry (optional).
 * @property stressLevel The stress level associated with the journal entry (optional).
 */
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
