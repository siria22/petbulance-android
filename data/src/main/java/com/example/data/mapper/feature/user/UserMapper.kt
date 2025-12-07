package com.example.data.mapper.feature.user

import com.example.data.datasource.remote.network.feature.user.dto.CheckProfileImageResDto
import com.example.data.datasource.remote.network.feature.user.dto.MeResponseDto
import com.example.data.datasource.remote.network.feature.user.dto.NicknameCheckResDto
import com.example.data.datasource.remote.network.feature.user.dto.NicknameSaveResDto
import com.example.data.datasource.remote.network.feature.user.dto.NicknameUpdateResDto
import com.example.data.datasource.remote.network.feature.user.dto.NotificationSettingReqDto
import com.example.data.datasource.remote.network.feature.user.dto.NotificationSettingResDto
import com.example.data.datasource.remote.network.feature.user.dto.ProfileImageUpdateResDto
import com.example.data.datasource.remote.network.feature.user.dto.SocialConnectResponseDto
import com.example.data.datasource.remote.network.feature.user.dto.SocialDisconnectResDto
import com.example.domain.model.feature.user.ConnectedSocials
import com.example.domain.model.feature.user.NicknameActionResult
import com.example.domain.model.feature.user.NicknameCheckResult
import com.example.domain.model.feature.user.NotificationSettings
import com.example.domain.model.feature.user.ProfileImageCheckResult
import com.example.domain.model.feature.user.ProfileImageUploadInfo
import com.example.domain.model.feature.user.SocialConnectResult
import com.example.domain.model.feature.user.UserInfo

fun NicknameCheckResDto.toDomain() = NicknameCheckResult(
    nickname = nickname,
    isAvailable = available,
    reason = reason
)

fun NicknameSaveResDto.toDomain() = NicknameActionResult(
    message = message
)

fun NicknameUpdateResDto.toDomain() = NicknameActionResult(
    message = message
)

fun SocialConnectResponseDto.toDomain() = SocialConnectResult(
    message = message
)

fun SocialDisconnectResDto.toDomain() = SocialConnectResult(
    message = message
)

fun ProfileImageUpdateResDto.toDomain() = ProfileImageUploadInfo(
    uploadUrl = preSignedUrl,
    resultUrl = imageUrl,
    fileId = saveId
)

fun CheckProfileImageResDto.toDomain() = ProfileImageCheckResult(
    message = message
)

fun MeResponseDto.toDomain() = UserInfo(
    nickname = nickname,
    profileImageUrl = profileImageUrl,
    email = email,
    provider = provider,
    connectedSocials = ConnectedSocials(
        kakao = kakaoEmail,
        google = googleEmail,
        naver = naverEmail
    )
)

fun NotificationSettings.toDto() = NotificationSettingReqDto(
    notificationsEnabled = isAllEnabled,
    eventNotificationsEnabled = isEventEnabled,
    marketingNotificationsEnabled = isMarketingEnabled
)

fun NotificationSettingResDto.toDomain() = NotificationSettings(
    isAllEnabled = notificationsEnabled,
    isEventEnabled = eventNotificationsEnabled,
    isMarketingEnabled = marketingNotificationsEnabled
)
