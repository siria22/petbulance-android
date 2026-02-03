package com.petbulance.data.datasource.remote.network.feature.hospital.hospital.dto

import kotlinx.serialization.Serializable

@Serializable
data class HospitalsResDto(
    val hospitalId: Long,
    val name: String,
    val lat: Double,
    val lng: Double,
    val distanceMeters: Double?,
    val phone: String?,
    val types: List<String>,
    val isOpenNow: Boolean,
    val openHours: String?,
    val thumbnailUrl: String?,
    val rating: Double?,
    val reviewCount: Int?,
    val image: String? = null,
    val tags: List<TagResDto>? = null
)

@Serializable
data class TagResDto(
    val type: String,
    val value: String
)