package com.example.domain.usecase.feature.hospital.review

import com.example.domain.model.feature.hospital.review.HospitalReview
import com.example.domain.model.feature.hospital.review.PagingReviewList
import com.example.domain.model.type.ReviewSortType
import com.example.domain.repository.feature.hospital.ReviewRepository
import javax.inject.Inject

class GetHospitalReviewsUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    suspend operator fun invoke(
        hospitalId: Long,
        onlyImageReview: Boolean,
        cursorId: Long?,
        cursorRating: Double?,
        cursorLikeCount: Long?,
        sortBy: ReviewSortType
    ): Result<PagingReviewList<HospitalReview>> {
        val direction = "desc"
        val sortType = when(sortBy) {
            ReviewSortType.MOST_HELPFUL -> "likeCount"
            ReviewSortType.LATEST -> "createdAt"
            ReviewSortType.RATING -> "totalRating"
        }

        return repository.getHospitalReviews(
            hospitalId = hospitalId,
            onlyImageReview = onlyImageReview,
            cursorId = cursorId,
            cursorRating = cursorRating,
            cursorLikeCount = cursorLikeCount,
            size = 10,
            sortBy = sortType,
            sortDirection = direction
        )
    }
}