package com.example.domain.usecase.feature.user.auth

import com.example.domain.model.feature.user.auth.SocialLoginResult
import com.example.domain.model.type.LoginProviderType
import com.example.domain.repository.feature.user.AuthRepository
import javax.inject.Inject

class SocialLoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(provider: LoginProviderType, authCode: String): Result<SocialLoginResult> {
        return repository.socialLogin(provider, authCode).onSuccess { result ->
            if (!result.accessToken.isNullOrEmpty() && !result.refreshToken.isNullOrEmpty()) {
                repository.saveTokens(result.accessToken, result.refreshToken)
                repository.saveLastLoginPlatform(provider)
            }
        }
    }
}