package com.fheinke.notero

import android.app.Application
import com.fheinke.notero.data.local.database.NoteroDatabase
import com.fheinke.notero.data.repository.JournalRepository
import com.fheinke.notero.data.repository.OfflineJournalRepository
import com.fheinke.notero.data.repository.OfflinePeriodRecordRepository
import androidx.room.Room
import com.fheinke.notero.data.repository.PeriodRecordRepository
import kotlin.jvm.java

/**
 * Custom Application class for Notero app.
 * Initializes the Room database and repositories for journal entries and period records.
 */
class NoteroApplication : Application() {
    val database: NoteroDatabase by lazy {
        Room.databaseBuilder(this, NoteroDatabase::class.java, "notero.db").build()
    }
    val journalRepository: JournalRepository by lazy {
        OfflineJournalRepository(database.journalEntryDao())
    }
    val periodRepository: PeriodRecordRepository by lazy {
        OfflinePeriodRecordRepository(database.periodRecordDao())
    }
}
