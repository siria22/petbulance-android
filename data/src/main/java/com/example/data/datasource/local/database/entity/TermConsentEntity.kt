package com.example.data.datasource.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "term_consents")
data class TermConsentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val version: String,
    val title: String,
    val content: String,
    val agreedDate: Long
)