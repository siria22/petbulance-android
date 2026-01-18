package com.example.domain.usecase.feature.hospital.review

import com.example.domain.model.feature.hospital.review.PagingReviewList
import com.example.domain.model.feature.hospital.review.ReviewSearchItem
import com.example.domain.repository.feature.hospital.ReviewRepository
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