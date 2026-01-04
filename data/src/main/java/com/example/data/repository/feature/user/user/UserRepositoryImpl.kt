package com.example.data.repository.feature.user.user

import com.example.data.datasource.remote.network.feature.user.user.UserApi
import com.example.data.datasource.remote.network.feature.user.user.dto.CheckProfileImageReqDto
import com.example.data.datasource.remote.network.feature.user.user.dto.CheckProfileImageResDto
import com.example.data.datasource.remote.network.feature.user.user.dto.MeResponseDto
import com.example.data.datasource.remote.network.feature.user.user.dto.NicknameCheckResDto
import com.example.data.datasource.remote.network.feature.user.user.dto.NicknameSaveRequestDto
import com.example.data.datasource.remote.network.feature.user.user.dto.NicknameSaveResDto
import com.example.data.datasource.remote.network.feature.user.user.dto.NicknameUpdateResDto
import com.example.data.datasource.remote.network.feature.user.user.dto.NotificationSettingResDto
import com.example.data.datasource.remote.network.feature.user.user.dto.ProfileImageUpdateReqDto
import com.example.data.datasource.remote.network.feature.user.user.dto.ProfileImageUpdateResDto
import com.example.data.datasource.remote.network.feature.user.user.dto.SocialConnectRequestDto
import com.example.data.datasource.remote.network.feature.user.user.dto.SocialConnectResponseDto
import com.example.data.datasource.remote.network.feature.user.user.dto.SocialDisconnectResDto
import com.example.data.datasource.remote.network.common.safeApiCall
import com.example.data.mapper.feature.user.toDomain
import com.example.data.mapper.feature.user.toDto
import com.example.domain.model.feature.user.user.NicknameActionResult
import com.example.domain.model.feature.user.user.NicknameCheckResult
import com.example.domain.model.feature.user.user.NotificationSettings
import com.example.domain.model.feature.user.user.ProfileImageCheckResult
import com.example.domain.model.feature.user.user.ProfileImageUploadInfo
import com.example.domain.model.feature.user.user.SocialConnectResult
import com.example.domain.model.feature.user.user.UserInfo
import com.example.domain.repository.feature.user.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi
) : UserRepository {

    override suspend fun checkNickname(nickname: String): Result<NicknameCheckResult> {
        return safeApiCall<NicknameCheckResDto>(path = "/users/nickname") {
            api.checkNickname(nickname)
        }.map { it.toDomain() }
    }

    override suspend fun saveNickname(nickname: String): Result<NicknameActionResult> {
        return safeApiCall<NicknameSaveResDto>(path = "/users/nickname") {
            val reqDto = NicknameSaveRequestDto(nickname = nickname)
            api.saveNickname(reqDto)
        }.map { it.toDomain() }
    }

    override suspend fun updateNickname(nickname: String): Result<NicknameActionResult> {
        return safeApiCall<NicknameUpdateResDto>(path = "/users/nickname") {
            val reqDto = NicknameSaveRequestDto(nickname = nickname)
            api.updateNickname(reqDto)
        }.map { it.toDomain() }
    }

    override suspend fun connectSocialAccount(
        provider: String,
        authCode: String
    ): Result<SocialConnectResult> {
        return safeApiCall<SocialConnectResponseDto>(path = "/users/social/connect") {
            val reqDto = SocialConnectRequestDto(provider = provider, authCode = authCode)
            api.connectSocialAccount(reqDto)
        }.map { it.toDomain() }
    }

    override suspend fun disconnectSocialAccount(platform: String): Result<SocialConnectResult> {
        return safeApiCall<SocialDisconnectResDto>(path = "/users/social/disconnect/$platform") {
            api.disconnectSocialAccount(platform)
        }.map { it.toDomain() }
    }

    override suspend fun requestProfileImageUpload(
        filename: String,
        mimeType: String
    ): Result<ProfileImageUploadInfo> {
        return safeApiCall<ProfileImageUpdateResDto>(path = "/users/profile") {
            val reqDto = ProfileImageUpdateReqDto(filename = filename, contentType = mimeType)
            api.updateProfileImage(reqDto)
        }.map { it.toDomain() }
    }

    override suspend fun checkProfileImageUpdate(
        saveId: String,
        filename: String
    ): Result<ProfileImageCheckResult> {
        return safeApiCall<CheckProfileImageResDto>(path = "/users/profile/success") {
            val reqDto = CheckProfileImageReqDto(saveId = saveId, filename = filename)
            api.checkProfileImageUpdate(reqDto)
        }.map { it.toDomain() }
    }

    override suspend fun getMyInfo(): Result<UserInfo> {
        return safeApiCall<MeResponseDto>(path = "/users/me") {
            api.getMyInfo()
        }.map { it.toDomain() }
    }

    override suspend fun updateNotificationSettings(
        settings: NotificationSettings
    ): Result<NotificationSettings> {
        return safeApiCall<NotificationSettingResDto>(path = "/users/settings/notification") {
            api.updateNotificationSettings(settings.toDto())
        }.map { it.toDomain() }
    }
}