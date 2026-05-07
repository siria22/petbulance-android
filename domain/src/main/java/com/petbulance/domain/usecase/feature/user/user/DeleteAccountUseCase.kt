package com.petbulance.domain.usecase.feature.user.user

import com.petbulance.domain.repository.feature.user.AuthRepository
import com.petbulance.domain.repository.feature.user.UserRepository
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return userRepository.deleteAccount().onSuccess {
            authRepository.clearTokens()
        }
    }
}
