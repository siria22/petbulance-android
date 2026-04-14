package com.petbulance.domain.repository.feature.user

import com.petbulance.domain.model.feature.user.user.AuthoritySettings

interface AuthorityRepository {
    suspend fun getAuthority(): Result<AuthoritySettings>
    suspend fun toggleAuthority(type: String): Result<Unit>
}
