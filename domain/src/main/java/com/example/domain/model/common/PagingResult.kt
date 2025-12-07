package com.example.domain.model.common

data class PagingResult<T>(
    val content: List<T>,
    val isLast: Boolean,
    val pageNumber: Int
)