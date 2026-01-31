package com.petbulance.data.datasource.remote.network.feature.hospital.hospital.dto

import kotlinx.serialization.Serializable

@Serializable
data class HospitalCardResDto(
    val hospitalId: Long,
    val name: String,
    val lat: Double,
    val lng: Double,
    val distanceMeters: Double,
    val phone: String,
    val types: List<String>,
    val isOpenNow: Boolean,
    val nextOpenHours: String? = null,
    val thumbnailUrl: String,
    val rating: Double? = 0.0,
    val reviewCount: Long,
    val image: String?
)
