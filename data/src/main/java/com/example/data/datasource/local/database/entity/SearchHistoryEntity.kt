package com.example.data.datasource.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0,
    val serverId: Long? = null,
    val keyword: String,
    val timestamp: Long,
    val isSynced: Boolean = false
)