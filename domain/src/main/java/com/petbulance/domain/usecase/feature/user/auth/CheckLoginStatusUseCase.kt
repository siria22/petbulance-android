package com.petbulance.domain.usecase.feature.user.auth

import com.petbulance.domain.repository.feature.user.AuthRepository
import javax.inject.Inject

class CheckLoginStatusUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        val accessToken = authRepository.getAccessToken().getOrNull()
        val refreshToken = authRepository.getRefreshToken().getOrNull()

        val isLoggedIn = !accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank()

        return Result.success(isLoggedIn)
    }
}