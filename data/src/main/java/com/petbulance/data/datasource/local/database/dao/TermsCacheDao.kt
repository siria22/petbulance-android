package com.petbulance.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.petbulance.data.datasource.local.database.entity.TermsCacheEntity

@Dao
interface TermsCacheDao {
    @Query("SELECT * FROM terms_cache")
    suspend fun getAllTerms(): List<TermsCacheEntity>

    @Query("SELECT cachedAt FROM terms_cache ORDER BY cachedAt DESC LIMIT 1")
    suspend fun getLatestCacheTime(): Long?

    @Query("SELECT * FROM terms_cache WHERE termsType = :type LIMIT 1")
    suspend fun getTermByType(type: String): TermsCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(term: TermsCacheEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(terms: List<TermsCacheEntity>)

    @Query("DELETE FROM terms_cache")
    suspend fun deleteAll()
}
