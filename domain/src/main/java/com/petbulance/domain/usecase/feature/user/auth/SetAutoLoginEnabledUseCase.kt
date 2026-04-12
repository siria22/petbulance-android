package com.petbulance.domain.usecase.feature.user.auth

import com.petbulance.domain.repository.feature.user.AuthRepository
import javax.inject.Inject

class SetAutoLoginEnabledUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(isEnabled: Boolean): Result<Unit> {
        return repository.setAutoLoginEnabled(isEnabled)
    }
}