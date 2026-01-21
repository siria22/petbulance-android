package com.petbulance.data.mapper.feature.user

import com.petbulance.data.datasource.remote.network.feature.user.user.dto.CheckProfileImageResDto
import com.petbulance.data.datasource.remote.network.feature.user.user.dto.MeResponseDto
import com.petbulance.data.datasource.remote.network.feature.user.user.dto.NicknameCheckResDto
import com.petbulance.data.datasource.remote.network.feature.user.user.dto.NicknameSaveResDto
import com.petbulance.data.datasource.remote.network.feature.user.user.dto.NicknameUpdateResDto
import com.petbulance.data.datasource.remote.network.feature.user.user.dto.NotificationSettingReqDto
import com.petbulance.data.datasource.remote.network.feature.user.user.dto.NotificationSettingResDto
import com.petbulance.data.datasource.remote.network.feature.user.user.dto.ProfileImageUpdateResDto
import com.petbulance.data.datasource.remote.network.feature.user.user.dto.SocialConnectResponseDto
import com.petbulance.data.datasource.remote.network.feature.user.user.dto.SocialDisconnectResDto
import com.petbulance.domain.model.feature.user.user.ConnectedSocials
import com.petbulance.domain.model.feature.user.user.NicknameActionResult
import com.petbulance.domain.model.feature.user.user.NicknameCheckResult
import com.petbulance.domain.model.feature.user.user.NotificationSettings
import com.petbulance.domain.model.feature.user.user.ProfileImageCheckResult
import com.petbulance.domain.model.feature.user.user.ProfileImageUploadInfo
import com.petbulance.domain.model.feature.user.user.SocialConnectResult
import com.petbulance.domain.model.feature.user.user.UserInfo

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
