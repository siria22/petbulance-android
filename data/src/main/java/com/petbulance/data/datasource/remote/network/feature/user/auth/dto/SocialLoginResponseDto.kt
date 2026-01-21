package com.petbulance.data.datasource.remote.network.feature.user.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class SocialLoginResponseDto(
    val isNewUser: Boolean,
    val signUpToken: String? = null,
    val firebaseCustomToken: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null
)