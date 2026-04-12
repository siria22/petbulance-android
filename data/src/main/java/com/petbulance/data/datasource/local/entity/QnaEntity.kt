package com.petbulance.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "qna")
data class QnaEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val content: String,
    val date: String,
    val status: String,
    val answerContent: String?,
    val answerDate: String?,
    val cachedAt: Long = System.currentTimeMillis()
)
