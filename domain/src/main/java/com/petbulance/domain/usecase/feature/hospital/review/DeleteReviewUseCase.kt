package com.petbulance.domain.usecase.feature.hospital.review

import com.petbulance.domain.repository.feature.hospital.ReviewRepository
import javax.inject.Inject

class DeleteReviewUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    suspend operator fun invoke(reviewId: Long): Result<String> {
        return repository.deleteMyReviews(listOf(reviewId))
    }
}