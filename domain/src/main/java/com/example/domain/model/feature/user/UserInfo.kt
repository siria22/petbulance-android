package com.example.domain.model.feature.user

data class UserInfo(
    val nickname: String,
    val profileImageUrl: String?,
    val email: String,
    val provider: String,
    val connectedSocials: ConnectedSocials
)
