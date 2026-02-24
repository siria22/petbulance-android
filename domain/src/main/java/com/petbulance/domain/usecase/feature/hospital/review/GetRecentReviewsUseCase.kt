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
            size = 10
        ).map { pagingResult ->
            pagingResult.items
                .filter { it.totalRating >= 4.0 }
                .take(3)
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
        }.getOrElse { emptyList() }
    }
}