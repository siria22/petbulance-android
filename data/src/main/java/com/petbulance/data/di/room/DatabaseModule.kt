package com.petbulance.data.di.room

import android.content.Context
import androidx.room.Room
import com.petbulance.data.datasource.local.dao.QnaDao
import com.petbulance.data.datasource.local.database.dao.SearchDao
import com.petbulance.data.datasource.local.database.dao.TermConsentDao
import com.petbulance.data.datasource.local.database.dao.TermsCacheDao
import com.petbulance.data.datasource.local.database.dao.TermsStatusDao
import com.petbulance.data.datasource.local.database.dao.ViewedHospitalDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    fun provideTermConsentDao(database: AppDatabase): TermConsentDao {
        return database.termConsentDao()
    }

    @Provides
    fun provideSearchDao(database: AppDatabase): SearchDao {
        return database.searchDao()
    }

    @Provides
    fun provideViewedHospitalDao(database: AppDatabase): ViewedHospitalDao {
        return database.viewedHospitalDao()
    }

    @Provides
    fun provideQnaDao(database: AppDatabase): QnaDao {
        return database.qnaDao()
    }

    @Provides
    fun provideTermsCacheDao(database: AppDatabase): TermsCacheDao {
        return database.termsCacheDao()
    }

    @Provides
    fun provideTermsStatusDao(database: AppDatabase): TermsStatusDao {
        return database.termsStatusDao()
    }
}