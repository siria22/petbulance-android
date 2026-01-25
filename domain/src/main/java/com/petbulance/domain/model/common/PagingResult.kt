package com.petbulance.domain.model.common

data class PagingResult<T>(
    val content: List<T>,
    val hasNext: Boolean,
    val cursorId: Long?,
    val cursorDistance: Double? = null,
    val cursorRating: Double? = null,
    val cursorReviewCount: Long? = null
)