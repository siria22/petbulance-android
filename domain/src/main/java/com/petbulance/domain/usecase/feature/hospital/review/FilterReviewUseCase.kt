package com.petbulance.domain.usecase.feature.hospital.review

import com.petbulance.domain.model.feature.hospital.review.PagingReviewList
import com.petbulance.domain.model.feature.hospital.review.ReviewSearchItem
import com.petbulance.domain.repository.feature.hospital.ReviewRepository
import javax.inject.Inject

class FilterReviewUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    suspend operator fun invoke(
        region: String?,
        animalTypes: List<String>?,
        isReceipt: Boolean?,
        cursorId: Long?,
        size: Int = 10
    ): Result<PagingReviewList<ReviewSearchItem>> {
        return repository.filterReview(
            region = region,
            animalTypes = animalTypes,
            isReceipt = isReceipt,
            cursorId = cursorId,
            size = size
        )
    }
}