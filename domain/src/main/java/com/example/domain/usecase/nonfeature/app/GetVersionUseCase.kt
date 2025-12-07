package com.example.domain.usecase.nonfeature.app

import com.example.domain.repository.nonfeature.app.AppInfoRepository
import javax.inject.Inject

class GetVersionUseCase @Inject constructor(
    private val repository: AppInfoRepository
) {
    suspend operator fun invoke(): String {
        return repository.getVersion().getOrThrow()
    }
}