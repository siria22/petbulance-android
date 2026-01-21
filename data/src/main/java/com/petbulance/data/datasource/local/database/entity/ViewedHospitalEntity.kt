package com.petbulance.data.datasource.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "viewed_hospital_history")
data class ViewedHospitalEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0,
    val hospitalId: Long,
    val hospitalName: String,
    val timestamp: Long,
    val isSynced: Boolean = false
)