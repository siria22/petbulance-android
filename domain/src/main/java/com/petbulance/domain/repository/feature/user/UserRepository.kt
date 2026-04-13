package com.petbulance.domain.repository.feature.user

import com.petbulance.domain.model.feature.user.user.NicknameActionResult
import com.petbulance.domain.model.feature.user.user.NicknameCheckResult
import com.petbulance.domain.model.feature.user.user.NotificationSettings
import com.petbulance.domain.model.feature.user.user.ProfileImageCheckResult
import com.petbulance.domain.model.feature.user.user.ProfileImageUploadInfo
import com.petbulance.domain.model.feature.user.user.SocialConnectResult
import com.petbulance.domain.model.feature.user.user.UserInfo

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

    suspend fun deleteAccount(): Result<Unit>

    suspend fun getMyInfo(): Result<UserInfo>

    suspend fun updateNotificationSettings(settings: NotificationSettings): Result<NotificationSettings>
}