package com.example.data.di.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.datasource.local.database.dao.ExampleDao
import com.example.data.datasource.local.database.entity.ExampleEntity

@Database(
    entities = [ExampleEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exampleDao(): ExampleDao
}