package com.example.data.datasource.remote.network.feature.user.user.dto

import kotlinx.serialization.Serializable

@Serializable
data class SocialConnectRequestDto(
    val provider: String, // ex: "kakao", "google"
    val authCode: String
)
