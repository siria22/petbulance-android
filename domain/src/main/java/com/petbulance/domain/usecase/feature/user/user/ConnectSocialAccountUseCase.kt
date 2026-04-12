package com.petbulance.domain.usecase.feature.user.user

import com.petbulance.domain.model.feature.user.user.SocialConnectResult
import com.petbulance.domain.repository.feature.user.UserRepository
import javax.inject.Inject

class ConnectSocialAccountUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(provider: String, authCode: String): Result<SocialConnectResult> {
        return repository.connectSocialAccount(provider = provider, authCode = authCode)
    }
}