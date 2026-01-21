package com.petbulance.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.petbulance.data.datasource.local.database.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {
    @Query("SELECT * FROM search_history ORDER BY timestamp DESC")
    fun getSearchHistoryStream(): Flow<List<SearchHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(entity: SearchHistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<SearchHistoryEntity>)

    @Query("SELECT * FROM search_history WHERE isSynced = 0")
    suspend fun getUnsyncedItems(): List<SearchHistoryEntity>

    @Query("UPDATE search_history SET isSynced = 1, serverId = :serverId WHERE keyword = :keyword")
    suspend fun markAsSynced(keyword: String, serverId: Long)

    @Query("DELETE FROM search_history WHERE keyword = :keyword")
    suspend fun deleteByKeyword(keyword: String)

    @Query("SELECT * FROM search_history WHERE keyword = :keyword LIMIT 1")
    suspend fun findByKeyword(keyword: String): SearchHistoryEntity?
}