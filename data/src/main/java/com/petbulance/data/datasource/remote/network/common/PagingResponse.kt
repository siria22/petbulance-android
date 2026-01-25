package com.petbulance.data.datasource.remote.network.common

import kotlinx.serialization.Serializable

@Serializable
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