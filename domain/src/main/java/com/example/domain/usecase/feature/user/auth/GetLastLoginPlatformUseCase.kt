package com.example.domain.usecase.feature.user.auth

import com.example.domain.model.type.LoginProviderType
import com.example.domain.repository.feature.user.AuthRepository
import javax.inject.Inject

class GetLastLoginPlatformUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Result<LoginProviderType?> {
        return repository.getLastLoginPlatform()
    }
}