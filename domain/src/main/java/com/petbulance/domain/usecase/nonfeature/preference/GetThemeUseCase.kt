package com.petbulance.domain.usecase.nonfeature.preference

import com.petbulance.domain.model.type.AppTheme
import com.petbulance.domain.repository.nonfeature.preference.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) {
    operator fun invoke(): Flow<Result<AppTheme>> {
        return preferenceRepository.getTheme().map { result ->
            result.map { AppTheme.fromString(it) }
        }
    }
}
