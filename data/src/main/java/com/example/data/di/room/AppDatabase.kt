package com.example.data.di.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.datasource.local.database.dao.SearchDao
import com.example.data.datasource.local.database.dao.TermConsentDao
import com.example.data.datasource.local.database.dao.ViewedHospitalDao
import com.example.data.datasource.local.database.entity.SearchHistoryEntity
import com.example.data.datasource.local.database.entity.TermConsentEntity
import com.example.data.datasource.local.database.entity.ViewedHospitalEntity

@Database(
    entities = [TermConsentEntity::class, SearchHistoryEntity::class, ViewedHospitalEntity::class], // Entity 추가
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun termConsentDao(): TermConsentDao
    abstract fun searchDao(): SearchDao
    abstract fun viewedHospitalDao(): ViewedHospitalDao
}