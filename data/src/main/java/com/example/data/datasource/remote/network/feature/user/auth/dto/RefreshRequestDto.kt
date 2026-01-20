package com.example.data.datasource.remote.network.feature.user.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequestDto(
    val refreshToken: String
)