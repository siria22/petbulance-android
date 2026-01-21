package com.petbulance.domain.usecase.feature.user.auth

import com.petbulance.domain.model.type.LoginProviderType
import com.petbulance.domain.repository.feature.user.AuthRepository
import javax.inject.Inject

class GetLastLoginPlatformUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Result<LoginProviderType?> {
        return repository.getLastLoginPlatform()
    }
}