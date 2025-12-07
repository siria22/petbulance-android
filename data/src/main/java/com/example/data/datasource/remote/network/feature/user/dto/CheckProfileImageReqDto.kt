package com.example.data.datasource.remote.network.feature.user.dto

import kotlinx.serialization.Serializable

@Serializable
data class CheckProfileImageReqDto(
    val saveId: String,
    val filename: String
)

@Serializable
data class CheckProfileImageResDto(
    val message: String
)

@Serializable
data class MeResponseDto(
    val provider: String,
    val email: String,
    val nickname: String,
    val profileImageUrl: String?,
    val kakaoEmail: String?,
    val googleEmail: String?,
    val naverEmail: String?
)

@Serializable
data class NotificationSettingReqDto(
    val notificationsEnabled: Boolean,
    val eventNotificationsEnabled: Boolean,
    val marketingNotificationsEnabled: Boolean
)

@Serializable
data class NotificationSettingResDto(
    val notificationsEnabled: Boolean,
    val eventNotificationsEnabled: Boolean,
    val marketingNotificationsEnabled: Boolean
)