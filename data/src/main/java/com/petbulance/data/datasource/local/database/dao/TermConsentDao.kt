package com.petbulance.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.petbulance.data.datasource.local.database.entity.TermConsentEntity

@Dao
interface TermConsentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConsents(consents: List<TermConsentEntity>)
}