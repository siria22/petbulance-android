package com.example.data.datasource.remote.network.feature.user.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class SocialLoginRequestDto(
    val provider: String, // "KAKAO", "NAVER", "GOOGLE"
    val authCode: String
)

