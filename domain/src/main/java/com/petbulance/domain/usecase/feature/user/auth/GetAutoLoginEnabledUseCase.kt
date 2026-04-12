package com.petbulance.domain.usecase.feature.user.auth

import com.petbulance.domain.repository.feature.user.AuthRepository
import javax.inject.Inject

class GetAutoLoginEnabledUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return repository.isAutoLoginEnabled()
    }
}