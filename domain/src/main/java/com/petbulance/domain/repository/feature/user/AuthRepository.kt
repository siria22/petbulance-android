package com.petbulance.domain.repository.feature.user

import com.petbulance.domain.model.feature.user.auth.SocialLoginResult
import com.petbulance.domain.model.type.LoginProviderType

interface AuthRepository {

    suspend fun saveTokens(accessToken: String, refreshToken: String): Result<Unit>
    suspend fun getAccessToken(): Result<String?>
    suspend fun getRefreshToken(): Result<String?>
    suspend fun clearTokens(): Result<Unit>
    suspend fun refreshToken(refreshToken: String): Result<Pair<String?, String?>>

    suspend fun socialLogin(provider: LoginProviderType, authCode: String): Result<SocialLoginResult>
    suspend fun logout(): Result<Unit>
    fun isLoggingOut(): Boolean

    suspend fun saveLastLoginPlatform(platform: LoginProviderType): Result<Unit>
    suspend fun getLastLoginPlatform(): Result<LoginProviderType?>

    suspend fun setAutoLoginEnabled(isEnabled: Boolean): Result<Unit>
    suspend fun isAutoLoginEnabled(): Result<Boolean>
}