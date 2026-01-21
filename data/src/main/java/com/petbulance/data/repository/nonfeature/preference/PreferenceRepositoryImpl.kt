package com.petbulance.data.repository.nonfeature.preference

import com.petbulance.data.datasource.local.preference.PreferenceProvider
import com.petbulance.domain.repository.nonfeature.preference.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferenceRepositoryImpl @Inject constructor(
    private val provider: PreferenceProvider
) : PreferenceRepository {
    override fun getTheme(): Flow<Result<String>> {
        return provider.observeTheme().map { Result.success(it) }
    }

    override suspend fun updateTheme(theme: String): Result<Unit> = runCatching {
        provider.updateTheme(theme)
    }
}