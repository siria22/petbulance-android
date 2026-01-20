package com.example.data.repository.feature.user.auth

import com.example.data.datasource.local.preference.PreferenceProvider
import com.example.data.datasource.remote.network.common.safeApiCall
import com.example.data.datasource.remote.network.feature.user.auth.AuthApi
import com.example.data.datasource.remote.network.feature.user.auth.dto.RefreshResponseDto
import com.example.data.datasource.remote.network.feature.user.auth.dto.SocialLoginRequestDto
import com.example.data.datasource.remote.network.feature.user.auth.dto.SocialLoginResponseDto
import com.example.data.di.security.AppKeyAlias
import com.example.data.di.security.CryptoManager
import com.example.data.di.security.toBase64String
import com.example.data.di.security.toEncryptedData
import com.example.domain.model.feature.user.auth.SocialLoginResult
import com.example.domain.model.type.LoginProviderType
import com.example.domain.repository.feature.user.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val preferenceProvider: PreferenceProvider,
    private val cryptoManager: CryptoManager,
) : AuthRepository {

    override suspend fun saveTokens(accessToken: String, refreshToken: String): Result<Unit> =
        runCatching {
            val encryptedAccessToken = cryptoManager.encrypt(
                alias = AppKeyAlias.ACCESS_TOKEN_KEY.name,
                data = accessToken,
                isBiometricRequired = false
            ) ?: throw IllegalStateException("Access token encryption failed")

            val encryptedRefreshToken = cryptoManager.encrypt(
                alias = AppKeyAlias.REFRESH_TOKEN_KEY.name,
                data = refreshToken,
                isBiometricRequired = false
            ) ?: throw IllegalStateException("Refresh token encryption failed")

            preferenceProvider.updateEncryptedAccessToken(encryptedAccessToken.toBase64String())
            preferenceProvider.updateEncryptedRefreshToken(encryptedRefreshToken.toBase64String())
        }

    override suspend fun getAccessToken(): Result<String?> = runCatching {
        val encryptedAccessToken = preferenceProvider.observeEncryptedAccessToken().first()

        encryptedAccessToken.toEncryptedData()?.let {
            cryptoManager.decrypt(AppKeyAlias.ACCESS_TOKEN_KEY.name, it)
        }
    }

    override suspend fun getRefreshToken(): Result<String?> = runCatching {
        val encryptedRefreshToken = preferenceProvider.observeEncryptedRefreshToken().first()

        encryptedRefreshToken.toEncryptedData()?.let {
            cryptoManager.decrypt(AppKeyAlias.REFRESH_TOKEN_KEY.name, it)
        }
    }

    override suspend fun clearTokens(): Result<Unit> = runCatching {
        preferenceProvider.updateEncryptedAccessToken("")
        preferenceProvider.updateEncryptedRefreshToken("")
    }

    override suspend fun refreshToken(refreshToken: String): Result<Pair<String?, String?>> =
        runCatching {
            return safeApiCall<RefreshResponseDto>("auth/refresh") {
                authApi.refresh(refreshToken)
            }.map { dto ->
                Pair(dto.accessToken, dto.refreshToken)
            }
        }

    override suspend fun socialLogin(provider: LoginProviderType, authCode: String): Result<SocialLoginResult> {
        return safeApiCall<SocialLoginResponseDto>("auth/social/login") {
            authApi.socialLogin(SocialLoginRequestDto(provider.name, authCode))
        }.map { dto ->
            SocialLoginResult(
                isNewUser = dto.isNewUser,
                signUpToken = dto.signUpToken,
                firebaseCustomToken = dto.firebaseCustomToken,
                accessToken = dto.accessToken,
                refreshToken = dto.refreshToken
            )
        }
    }

    override suspend fun logout(): Result<Unit> {
        return safeApiCall<Unit>("auth/logout") {
            authApi.logout()
        }.onSuccess {
            clearTokens()
        }
    }

    override suspend fun saveLastLoginPlatform(platform: LoginProviderType): Result<Unit> =
        runCatching {
            preferenceProvider.updateLastLoginPlatform(platform.name)
        }

    override suspend fun getLastLoginPlatform(): Result<LoginProviderType?> =
        runCatching {
            val platformName = preferenceProvider.observeLastLoginPlatform().first()
            if (platformName.isNotEmpty()) {
                LoginProviderType.fromString(platformName)
            } else {
                null
            }
        }
}