package com.petbulance.domain.usecase.nonfeature.app

import com.petbulance.domain.repository.nonfeature.app.AppInfoRepository
import javax.inject.Inject

class GetAppVersionUseCase @Inject constructor(
    private val repository: AppInfoRepository
) {
    suspend operator fun invoke(): String {
        return repository.getVersion().getOrThrow()
    }
}