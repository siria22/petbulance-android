package com.example.data.mapper.feature.user

import com.example.data.datasource.remote.network.feature.user.user.dto.CheckProfileImageResDto
import com.example.data.datasource.remote.network.feature.user.user.dto.MeResponseDto
import com.example.data.datasource.remote.network.feature.user.user.dto.NicknameCheckResDto
import com.example.data.datasource.remote.network.feature.user.user.dto.NicknameSaveResDto
import com.example.data.datasource.remote.network.feature.user.user.dto.NicknameUpdateResDto
import com.example.data.datasource.remote.network.feature.user.user.dto.NotificationSettingReqDto
import com.example.data.datasource.remote.network.feature.user.user.dto.NotificationSettingResDto
import com.example.data.datasource.remote.network.feature.user.user.dto.ProfileImageUpdateResDto
import com.example.data.datasource.remote.network.feature.user.user.dto.SocialConnectResponseDto
import com.example.data.datasource.remote.network.feature.user.user.dto.SocialDisconnectResDto
import com.example.domain.model.feature.user.user.ConnectedSocials
import com.example.domain.model.feature.user.user.NicknameActionResult
import com.example.domain.model.feature.user.user.NicknameCheckResult
import com.example.domain.model.feature.user.user.NotificationSettings
import com.example.domain.model.feature.user.user.ProfileImageCheckResult
import com.example.domain.model.feature.user.user.ProfileImageUploadInfo
import com.example.domain.model.feature.user.user.SocialConnectResult
import com.example.domain.model.feature.user.user.UserInfo

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
