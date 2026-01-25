package com.petbulance.domain.usecase.feature.hospital.review

import com.petbulance.domain.model.feature.hospital.review.ReceiptAnalysisResult
import com.petbulance.domain.repository.feature.hospital.ReviewRepository
import javax.inject.Inject

class AnalyzeReceiptUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    suspend operator fun invoke(
        imageBytes: ByteArray,
        fileName: String = "receipt.jpg"
    ): Result<ReceiptAnalysisResult> {
        return repository.analyzeReceipt(imageBytes, fileName)
    }
}