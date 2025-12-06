package com.example.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.datasource.local.database.entity.ExampleEntity

@Dao
interface ExampleDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun createExampleEntity(exampleEntity: ExampleEntity)

    @Query("SELECT * FROM example")
    suspend fun getAllExampleEntity(): List<ExampleEntity>

    @Delete
    suspend fun deleteExampleEntity(exampleEntity: ExampleEntity)
}