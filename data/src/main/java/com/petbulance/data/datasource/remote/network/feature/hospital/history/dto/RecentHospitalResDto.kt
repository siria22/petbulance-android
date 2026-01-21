package com.petbulance.data.datasource.remote.network.feature.hospital.history.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecentHospitalResDto(
    val keywordId: Long,
    val keyword: String,
    val createdAt: String // Server: LocalDateTime
)

@Serializable
data class RecentHospitalSaveReqDto(
    val keyword: String
)

@Serializable
data class RecentHospitalSaveResDto(
    val keywordId: Long,
    val keyword: String,
    val createdAt: String
)

@Serializable
data class ViewedHospitalSaveReqDto(
    val hospitalId: Long
)

@Serializable
data class ViewedHospitalSaveResDto(
    val id: Long, // 기록 ID (View History ID)
    val viewedAt: String
)

@Serializable
data class ViewedHospitalResDto(
    val viewedHospitals: List<ViewedHospitalDto>,
    val total: Long
)

