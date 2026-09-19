package com.fheinke.notero.data.local.database

import com.fheinke.notero.data.local.entities.*
import com.fheinke.notero.data.local.dao.*
import com.fheinke.notero.data.local.converter.LocalDateConverter
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * The Room database for the Notero app.
 *
 * This class defines the database configuration and serves as the main access point for the underlying connection to the app's persisted data.
 * It includes the entities that represent the tables in the database and provides abstract methods to access the DAOs (Data Access Objects) for each entity.
 */
@Database(
    entities = [
        JournalEntryEntity::class,
        PeriodRecordEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateConverter::class)
abstract class NoteroDatabase : RoomDatabase() {

    /**
     * Provides access to the JournalEntryDao.
     *
     * @return The JournalEntryDao instance.
     */
    abstract fun journalEntryDao(): JournalEntryDao

    /**
     * Provides access to the PeriodRecordDao.
     *
     * @return The PeriodRecordDao instance.
     */
    abstract fun periodRecordDao(): PeriodRecordDao
}
