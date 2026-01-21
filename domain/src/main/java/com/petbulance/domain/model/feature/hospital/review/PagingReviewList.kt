package com.petbulance.domain.model.feature.hospital.review

data class PagingReviewList<T>(
    val items: List<T>,
    val nextCursorId: Long?,
    val hasNext: Boolean
)