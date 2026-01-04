package com.example.data.datasource.remote.network.feature.user.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshResponseDto(
    val accessToken: String? = null,
    val refreshToken: String? = null
)