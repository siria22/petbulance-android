package com.example.domain.usecase.nonfeature.app

import com.example.domain.repository.nonfeature.app.AppInfoRepository
import javax.inject.Inject

class CheckAppVersionUseCase @Inject constructor(
    private val repository: AppInfoRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return repository.getVersion().mapCatching { serverVersion ->
            val currentVersion = repository.getCurrentAppVersion().getOrThrow()
            serverVersion != currentVersion
        }
    }
}