package com.petbulance.domain.usecase.feature.user.auth

import com.petbulance.domain.repository.feature.user.AuthRepository
import com.petbulance.domain.repository.feature.user.UserRepository
import javax.inject.Inject

class CheckLoginStatusUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        val accessToken = authRepository.getAccessToken().getOrNull()
        val refreshToken = authRepository.getRefreshToken().getOrNull()

        if (accessToken.isNullOrEmpty() || refreshToken.isNullOrEmpty()) {
            return Result.success(false)
        }

        return userRepository.getMyInfo()
            .map { true }
            .recoverCatching {
                authRepository.clearTokens()
                false
            }
    }
}