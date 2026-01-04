package com.example.data.datasource.remote.network.feature.user.user.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProfileImageUpdateResDto(
    val preSignedUrl: String,
    val imageUrl: String,
    val saveId: String
)