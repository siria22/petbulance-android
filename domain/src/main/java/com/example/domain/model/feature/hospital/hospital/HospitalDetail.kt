package com.example.domain.model.feature.hospital.hospital

/**
 * From HospitalDetailResDto
 */
data class HospitalDetail(
    val hospitalId: Long,
    val name: String,
    val address: String,
    val lat: Double,
    val lng: Double,
    val phone: String,
    val acceptedAnimals: List<String>,
    val openHours: List<OpenHour>,
    val notes: String,
    val openNow: Boolean,
    val description: String
)

data class OpenHour(
    val openHour: String,
    val closeHour: String
)