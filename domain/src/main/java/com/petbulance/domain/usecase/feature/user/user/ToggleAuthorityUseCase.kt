package com.petbulance.domain.usecase.feature.user.user

import com.petbulance.domain.repository.feature.user.AuthorityRepository
import javax.inject.Inject

class ToggleAuthorityUseCase @Inject constructor(
    private val repository: AuthorityRepository
) {
    suspend operator fun invoke(type: String): Result<Unit> {
        return repository.toggleAuthority(type)
    }
}
