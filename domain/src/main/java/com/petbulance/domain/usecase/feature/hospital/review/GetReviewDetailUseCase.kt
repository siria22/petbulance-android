package com.petbulance.domain.usecase.feature.hospital.review

import com.petbulance.domain.model.feature.hospital.review.ReviewDetail
import com.petbulance.domain.model.feature.hospital.review.ReviewSearchItem
import com.petbulance.domain.repository.feature.hospital.ReviewRepository
import javax.inject.Inject

class GetReviewDetailUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    suspend operator fun invoke(reviewId: Long): Result<ReviewDetail> {
        return repository.getReviewDetail(reviewId)
    }
}