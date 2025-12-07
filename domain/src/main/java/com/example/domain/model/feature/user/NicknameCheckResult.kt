package com.example.domain.model.feature.user

data class NicknameCheckResult(
    val nickname: String,
    val isAvailable: Boolean,
    val reason: String?
)
