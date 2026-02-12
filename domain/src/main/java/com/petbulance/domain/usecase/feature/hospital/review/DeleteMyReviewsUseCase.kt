package com.petbulance.domain.usecase.feature.hospital.review

import com.petbulance.domain.repository.feature.hospital.ReviewRepository
import javax.inject.Inject

class DeleteMyReviewsUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(ids: List<Long>): Result<String> {
        return reviewRepository.deleteMyReviews(ids)
    }
}