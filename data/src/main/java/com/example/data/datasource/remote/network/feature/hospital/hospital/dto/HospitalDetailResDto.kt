package com.example.data.datasource.remote.network.feature.hospital.hospital.dto

import kotlinx.serialization.Serializable

@Serializable
data class HospitalDetailResDto(
    val hospitalId: Long,
    val name: String,
    val address: String,
    val lat: Double,
    val lng: Double,
    val phone: String,
    val acceptedAnimals: List<String>,
    val openHours: List<OpenHourResDto>,
    val notes: String,
    val openNow: Boolean,
    val description: String
)

@Serializable
data class OpenHourResDto(
    val openHour: String,
    val closeHour: String
)