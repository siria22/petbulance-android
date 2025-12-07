package com.example.domain.repository.feature.user

import com.example.domain.model.feature.user.NicknameActionResult
import com.example.domain.model.feature.user.NicknameCheckResult
import com.example.domain.model.feature.user.NotificationSettings
import com.example.domain.model.feature.user.ProfileImageCheckResult
import com.example.domain.model.feature.user.ProfileImageUploadInfo
import com.example.domain.model.feature.user.SocialConnectResult
import com.example.domain.model.feature.user.UserInfo

interface UserRepository {
    suspend fun checkNickname(nickname: String): Result<NicknameCheckResult>
    suspend fun saveNickname(nickname: String): Result<NicknameActionResult>
    suspend fun updateNickname(nickname: String): Result<NicknameActionResult>
    suspend fun connectSocialAccount(
        provider: String,
        authCode: String
    ): Result<SocialConnectResult>

    suspend fun disconnectSocialAccount(platform: String): Result<SocialConnectResult>
    suspend fun requestProfileImageUpload(
        filename: String,
        mimeType: String
    ): Result<ProfileImageUploadInfo>

    suspend fun checkProfileImageUpdate(saveId: String, filename: String): Result<ProfileImageCheckResult>

    suspend fun getMyInfo(): Result<UserInfo>

    suspend fun updateNotificationSettings(settings: NotificationSettings): Result<NotificationSettings>
}