package com.petbulance.domain.usecase.feature.hospital.review

import com.petbulance.domain.model.feature.hospital.review.PagingReviewList
import com.petbulance.domain.model.feature.hospital.review.ReviewSearchItem
import com.petbulance.domain.repository.feature.hospital.ReviewRepository
import javax.inject.Inject

class SearchReviewUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    suspend operator fun invoke(
        query: String,
        cursorId: Long? = null
    ): Result<PagingReviewList<ReviewSearchItem>> {
        return repository.searchReview(
            query = query,
            cursorId = cursorId,
            size = 10
        )
    }
}