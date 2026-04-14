package com.petbulance.domain.usecase.feature.user.user

import com.petbulance.domain.model.feature.user.user.AuthoritySettings
import com.petbulance.domain.repository.feature.user.AuthorityRepository
import javax.inject.Inject

class GetAuthorityUseCase @Inject constructor(
    private val repository: AuthorityRepository
) {
    suspend operator fun invoke(): Result<AuthoritySettings> {
        return repository.getAuthority()
    }
}
