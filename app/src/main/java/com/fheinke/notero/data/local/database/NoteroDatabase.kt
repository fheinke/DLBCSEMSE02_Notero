package com.fheinke.notero.data.local.database

import com.fheinke.notero.data.local.entities.*
import com.fheinke.notero.data.local.dao.*
import com.fheinke.notero.data.local.converter.LocalDateConverter
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        JournalEntryEntity::class,
        AttachmentEntity::class,
        PeriodRecordEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(LocalDateConverter::class)
abstract class NoteroDatabase : RoomDatabase() {
    abstract fun journalEntryDao(): JournalEntryDao
    abstract fun attachmentDao(): AttachmentDao
    abstract fun periodRecordDao(): PeriodRecordDao
}
