package com.petbulance.data.datasource.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "terms_cache")
data class TermsCacheEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val termsType: String?,
    val required: Boolean,
    val summary: String,
    val content: String,
    val version: String,
    val cachedAt: Long
)
