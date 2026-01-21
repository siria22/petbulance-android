package com.petbulance.data.datasource.remote.network.feature.hospital.history.dto

import kotlinx.serialization.Serializable

@Serializable
data class ViewedHospitalDto(
    val hospitalId: Long,
    val name: String,
    val viewedAt: String
)