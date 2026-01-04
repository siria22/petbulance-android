package com.example.domain.model.feature.user.auth

data class SocialLoginResult(
    val isNewUser: Boolean,
    val signUpToken: String? = null,
    val firebaseCustomToken: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null
)