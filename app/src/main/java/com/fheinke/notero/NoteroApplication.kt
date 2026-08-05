package com.fheinke.notero

import com.fheinke.notero.data.local.database.NoteroDatabase
import com.fheinke.notero.data.repository.JournalRepository
import com.fheinke.notero.data.repository.OfflineJournalRepository
import com.fheinke.notero.data.repository.OfflinePeriodRecordRepository
import android.app.Application
import androidx.room.Room

class NoteroApplication : Application() {
    lateinit var database: NoteroDatabase
        private set

    lateinit var journalRepository: JournalRepository
        private set
    lateinit var periodRecordRepository: OfflinePeriodRecordRepository
        private set

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            NoteroDatabase::class.java,
            "notero.db"
        ).build()

        journalRepository = OfflineJournalRepository(
            journalEntryDao = database.journalEntryDao(),
            attachmentDao = database.attachmentDao()
        )

        periodRecordRepository = OfflinePeriodRecordRepository(
            periodRecordDao = database.periodRecordDao()
        )
    }
}
