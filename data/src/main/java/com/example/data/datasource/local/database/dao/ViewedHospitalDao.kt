package com.example.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.datasource.local.database.entity.ViewedHospitalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ViewedHospitalDao {
    @Query("SELECT * FROM viewed_hospital_history ORDER BY timestamp DESC")
    fun getViewedHospitalStream(): Flow<List<ViewedHospitalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(entity: ViewedHospitalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<ViewedHospitalEntity>)

    @Query("DELETE FROM viewed_hospital_history WHERE hospitalId = :hospitalId")
    suspend fun deleteByHospitalId(hospitalId: Long)

    @Query("SELECT * FROM viewed_hospital_history WHERE hospitalId = :hospitalId LIMIT 1")
    suspend fun findByHospitalId(hospitalId: Long): ViewedHospitalEntity?

    @Query("UPDATE viewed_hospital_history SET isSynced = 1 WHERE hospitalId = :hospitalId")
    suspend fun markAsSynced(hospitalId: Long)
}