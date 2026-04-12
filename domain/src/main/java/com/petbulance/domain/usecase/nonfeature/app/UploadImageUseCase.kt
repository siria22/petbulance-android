package com.petbulance.domain.usecase.nonfeature.app

import com.petbulance.domain.repository.nonfeature.app.AppInfoRepository
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(
    private val appInfoRepository: AppInfoRepository
) {
    suspend operator fun invoke(url: String, imageBytes: ByteArray, mimeType: String): Result<Unit> {
        return appInfoRepository.uploadImage(url, imageBytes, mimeType)
    }
}
