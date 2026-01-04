package com.example.data.repository.feature.user.user

import com.example.domain.model.feature.user.user.ConnectedSocials
import com.example.domain.model.feature.user.user.NicknameActionResult
import com.example.domain.model.feature.user.user.NicknameCheckResult
import com.example.domain.model.feature.user.user.NotificationSettings
import com.example.domain.model.feature.user.user.ProfileImageCheckResult
import com.example.domain.model.feature.user.user.ProfileImageUploadInfo
import com.example.domain.model.feature.user.user.SocialConnectResult
import com.example.domain.model.feature.user.user.UserInfo
import com.example.domain.repository.feature.user.UserRepository
import javax.inject.Inject

class MockUserRepository @Inject constructor() : UserRepository {

    override suspend fun checkNickname(nickname: String): Result<NicknameCheckResult> {
        return Result.success(
            NicknameCheckResult(
                nickname = "HappyEnergy ^o^",
                isAvailable = true,
                reason = ""
            )
        )
    }

    override suspend fun saveNickname(nickname: String): Result<NicknameActionResult> {
        return Result.success(NicknameActionResult(""))
    }

    override suspend fun updateNickname(nickname: String): Result<NicknameActionResult> {
        return Result.success(NicknameActionResult(""))
    }

    override suspend fun connectSocialAccount(
        provider: String,
        authCode: String
    ): Result<SocialConnectResult> {
        return Result.success(SocialConnectResult(""))
    }

    override suspend fun disconnectSocialAccount(platform: String): Result<SocialConnectResult> {
        return Result.success(SocialConnectResult(""))
    }

    override suspend fun requestProfileImageUpload(
        filename: String,
        mimeType: String
    ): Result<ProfileImageUploadInfo> {
        return Result.success(
            ProfileImageUploadInfo(
                uploadUrl = "",
                resultUrl = "",
                fileId = ""
            )
        )
    }

    override suspend fun checkProfileImageUpdate(
        saveId: String,
        filename: String
    ): Result<ProfileImageCheckResult> {
        return Result.success(ProfileImageCheckResult("프로필 이미지가 성공적으로 업데이트되었습니다."))
    }

    override suspend fun getMyInfo(): Result<UserInfo> {
        return Result.success(
            UserInfo(
                nickname = "펫뷸런스",
                profileImageUrl = "https://petbulance.com/profile/default.png",
                email = "contact@petbulance.com",
                provider = "KAKAO",
                connectedSocials = ConnectedSocials(
                    kakao = "connected",
                    google = null,
                    naver = null
                )
            )
        )
    }

    override suspend fun updateNotificationSettings(settings: NotificationSettings): Result<NotificationSettings> {
        // Simply return the settings that were passed in, simulating a successful update.
        return Result.success(settings)
    }

}