package com.fheinke.notero.data.local.dao

import com.fheinke.notero.data.local.entities.PeriodRecordEntity
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PeriodRecordDao {
    @Query("SELECT * FROM period_records ORDER BY startDate DESC")
    fun observeAll(): Flow<List<PeriodRecordEntity>>

    @Query("SELECT * FROM period_records WHERE id = :id")
    suspend fun getById(id: Long): PeriodRecordEntity?

    @Upsert
    suspend fun upsert(record: PeriodRecordEntity)

    @Delete
    suspend fun delete(record: PeriodRecordEntity)

    @Query("DELETE FROM period_records WHERE id = :id")
    suspend fun deleteById(id: Long)
}
