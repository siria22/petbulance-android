package com.petbulance.domain.usecase.feature.hospital.review

import com.petbulance.domain.model.feature.hospital.review.MyReview
import com.petbulance.domain.model.feature.hospital.review.PagingReviewList
import com.petbulance.domain.repository.feature.hospital.ReviewRepository
import javax.inject.Inject

class GetMyReviewsUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(
        size: Int = 10,
        cursorId: Long? = null
    ): Result<PagingReviewList<MyReview>> {
        return reviewRepository.getMyReviews(size, cursorId)
    }
}