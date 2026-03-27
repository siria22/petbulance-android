package com.petbulance.domain.usecase.feature.hospital.review

import com.petbulance.domain.model.feature.home.HomeScreenReview
import com.petbulance.domain.repository.feature.hospital.ReviewRepository
import javax.inject.Inject

class GetRecentReviewsUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    suspend operator fun invoke(): List<HomeScreenReview> {
        return repository.filterReview(
            region = null,
            animalTypes = null,
            isReceipt = null,
            cursorId = null,
            size = PAGE_SIZE
        ).map { pagingResult ->
            pagingResult.items
                .filter { it.totalRating >= MIN_RATING_THRESHOLD }
                .take(MAX_DISPLAY_COUNT)
                .map { reviewItem ->
                    HomeScreenReview(
                        id = reviewItem.id,
                        hospitalName = reviewItem.hospitalName,
                        rating = reviewItem.totalRating,
                        reviewCount = 0, // API에서 제공하지 않음
                        image = reviewItem.images.firstOrNull() ?: reviewItem.hospitalImage,
                        content = reviewItem.reviewContent
                    )
                }
        }.getOrThrow()
    }

    companion object {
        private const val MIN_RATING_THRESHOLD = 4.0
        private const val MAX_DISPLAY_COUNT = 3
        private const val PAGE_SIZE = 10
    }
}
