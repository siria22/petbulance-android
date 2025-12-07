package com.example.data.datasource.remote.network.common

data class PagingResponse<T>(
    val content: List<T>,
    val last: Boolean,
    val totalPages: Int,
    val totalElements: Long,
    val size: Int,
    val number: Int,
    val first: Boolean,
    val numberOfElements: Int,
    val empty: Boolean
)