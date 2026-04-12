package com.petbulance.data.repository.feature.user.auth

import com.petbulance.data.datasource.local.preference.PreferenceProvider
import com.petbulance.data.datasource.remote.network.common.safeApiCall
import com.petbulance.data.datasource.remote.network.feature.user.auth.AuthApi
import com.petbulance.data.datasource.remote.network.feature.user.auth.dto.RefreshResponseDto
import com.petbulance.data.datasource.remote.network.feature.user.auth.dto.SocialLoginRequestDto
import com.petbulance.data.datasource.remote.network.feature.user.auth.dto.SocialLoginResponseDto
import com.petbulance.data.di.security.AppKeyAlias
import com.petbulance.data.di.security.CryptoManager
import com.petbulance.data.di.security.toBase64String
import com.petbulance.data.di.security.toEncryptedData
import com.petbulance.domain.model.feature.user.auth.SocialLoginResult
import com.petbulance.domain.model.type.LoginProviderType
import com.petbulance.domain.repository.feature.user.AuthRepository
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
                val cleanAccessToken = dto.accessToken?.removePrefix("Bearer ")
                Pair(cleanAccessToken, dto.refreshToken)
            }
        }

    override suspend fun socialLogin(
        provider: LoginProviderType,
        authCode: String
    ): Result<SocialLoginResult> {
        return safeApiCall<SocialLoginResponseDto>("auth/social/login") {
            authApi.socialLogin(SocialLoginRequestDto(provider.name, authCode))
        }.map { dto ->
            SocialLoginResult(
                isNewUser = dto.isNewUser,
                signUpToken = dto.signUpToken,
                firebaseCustomToken = dto.firebaseCustomToken,
                accessToken = dto.accessToken?.removePrefix("Bearer "),
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

    override suspend fun setAutoLoginEnabled(isEnabled: Boolean): Result<Unit> = runCatching {
        preferenceProvider.updateAutoLoginEnabled(isEnabled)
    }

    override suspend fun isAutoLoginEnabled(): Result<Boolean> = runCatching {
        preferenceProvider.observeAutoLoginEnabled().first()
    }
}