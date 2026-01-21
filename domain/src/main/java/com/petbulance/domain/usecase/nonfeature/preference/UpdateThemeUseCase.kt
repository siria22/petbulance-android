package com.petbulance.domain.usecase.nonfeature.preference

import com.petbulance.domain.model.type.AppTheme
import com.petbulance.domain.repository.nonfeature.preference.PreferenceRepository
import javax.inject.Inject

class UpdateThemeUseCase @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) {
    suspend operator fun invoke(theme: AppTheme): Result<Unit> {
        return preferenceRepository.updateTheme(theme.name)
    }
}
