package com.petbulance.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.petbulance.data.datasource.local.entity.QnaEntity

@Dao
interface QnaDao {
    @Query("SELECT * FROM qna ORDER BY id DESC")
    suspend fun getAllQna(): List<QnaEntity>

    @Query("SELECT * FROM qna WHERE id = :qnaId")
    suspend fun getQnaById(qnaId: Long): QnaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(qnaList: List<QnaEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(qna: QnaEntity)

    @Query("DELETE FROM qna WHERE id = :qnaId")
    suspend fun deleteById(qnaId: Long)

    @Query("DELETE FROM qna")
    suspend fun deleteAll()

    @Query("SELECT MAX(cachedAt) FROM qna")
    suspend fun getLatestCacheTime(): Long?
}
