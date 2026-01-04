package com.example.domain.repository.feature.user

import com.example.domain.model.feature.user.auth.SocialLoginResult
import com.example.domain.model.type.LoginProviderType

interface AuthRepository {

    suspend fun saveTokens(accessToken: String, refreshToken: String): Result<Unit>
    suspend fun getAccessToken(): Result<String?>
    suspend fun getRefreshToken(): Result<String?>
    suspend fun clearTokens(): Result<Unit>
    suspend fun refreshToken(refreshToken: String): Result<Pair<String?, String?>>

    suspend fun socialLogin(provider: LoginProviderType, authCode: String): Result<SocialLoginResult>
    suspend fun logout(): Result<Unit>

    suspend fun saveLastLoginPlatform(platform: LoginProviderType): Result<Unit>
    suspend fun getLastLoginPlatform(): Result<LoginProviderType?>
}