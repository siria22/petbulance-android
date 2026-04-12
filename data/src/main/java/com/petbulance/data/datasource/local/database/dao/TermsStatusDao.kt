package com.petbulance.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.petbulance.data.datasource.local.database.entity.TermsStatusEntity

@Dao
interface TermsStatusDao {
    @Query("SELECT * FROM terms_status WHERE id = 1")
    suspend fun getTermsStatus(): TermsStatusEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(termsStatus: TermsStatusEntity)

    @Query("DELETE FROM terms_status")
    suspend fun deleteAll()
}
