package com.fheinke.notero

import com.fheinke.notero.data.local.database.NoteroDatabase
import com.fheinke.notero.data.repository.JournalRepository
import com.fheinke.notero.data.repository.OfflineJournalRepository
import com.fheinke.notero.data.repository.OfflinePeriodRecordRepository
import com.fheinke.notero.data.preferences.UserPreferencesRepository
import com.fheinke.notero.ui.overview.MainActivity
import com.fheinke.notero.ui.welcome.WelcomeActivity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.room.Room
import kotlin.jvm.java

class NoteroApplication : ComponentActivity() {
    lateinit var database: NoteroDatabase
        private set

    lateinit var journalRepository: JournalRepository
        private set
    lateinit var periodRecordRepository: OfflinePeriodRecordRepository
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        val userPreferencesRepository = UserPreferencesRepository(applicationContext)

        val targetActivity = if (userPreferencesRepository.isOnboardingCompleted()) {
            MainActivity::class.java
        } else {
            WelcomeActivity::class.java
        }

        startActivity(Intent(this, targetActivity))
        finish()
    }
}
