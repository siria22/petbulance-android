package com.petbulance.data.repository.feature.user.auth

import com.petbulance.domain.model.feature.user.auth.SocialLoginResult
import com.petbulance.domain.model.type.LoginProviderType
import com.petbulance.domain.repository.feature.user.AuthRepository
import javax.inject.Inject

class MockAuthRepository @Inject constructor() : AuthRepository {
    override suspend fun saveTokens(accessToken: String, refreshToken: String): Result<Unit> = Result.success(Unit)
    override suspend fun getAccessToken(): Result<String?> = Result.success("mock_access_token")
    override suspend fun getRefreshToken(): Result<String?> = Result.success("mock_refresh_token")
    override suspend fun clearTokens(): Result<Unit> = Result.success(Unit)
    override suspend fun refreshToken(refreshToken: String): Result<Pair<String?, String?>> =
        Result.success("new_access_token" to "new_refresh_token")

    override suspend fun socialLogin(provider: LoginProviderType, authCode: String): Result<SocialLoginResult> =
        Result.success(SocialLoginResult(isNewUser = false, accessToken = "mock_token", refreshToken = "mock_token"))

    override suspend fun logout(): Result<Unit> = Result.success(Unit)
    override suspend fun saveLastLoginPlatform(platform: LoginProviderType): Result<Unit> = Result.success(Unit)
    override suspend fun getLastLoginPlatform(): Result<LoginProviderType?> = Result.success(LoginProviderType.KAKAO)
    override suspend fun setAutoLoginEnabled(isEnabled: Boolean): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun isAutoLoginEnabled(): Result<Boolean> {
        TODO("Not yet implemented")
    }
}