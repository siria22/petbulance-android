package com.petbulance.data.datasource.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "terms_status")
data class TermsStatusEntity(
    @PrimaryKey val id: Int = 1, // 단일 레코드만 유지
    val service: Boolean,
    val privacy: Boolean,
    val location: Boolean,
    val marketing: Boolean,
    val cachedAt: Long
)
