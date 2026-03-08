package com.petbulance.data.di.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.petbulance.data.datasource.local.dao.QnaDao
import com.petbulance.data.datasource.local.database.dao.SearchDao
import com.petbulance.data.datasource.local.database.dao.TermConsentDao
import com.petbulance.data.datasource.local.database.dao.ViewedHospitalDao
import com.petbulance.data.datasource.local.database.entity.SearchHistoryEntity
import com.petbulance.data.datasource.local.database.entity.TermConsentEntity
import com.petbulance.data.datasource.local.database.entity.ViewedHospitalEntity
import com.petbulance.data.datasource.local.entity.QnaEntity

@Database(
    entities = [TermConsentEntity::class, SearchHistoryEntity::class, ViewedHospitalEntity::class, QnaEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(LocalDateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun termConsentDao(): TermConsentDao
    abstract fun searchDao(): SearchDao
    abstract fun viewedHospitalDao(): ViewedHospitalDao
    abstract fun qnaDao(): QnaDao
}