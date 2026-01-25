package com.petbulance.data.datasource.remote.network.common

import kotlinx.serialization.Serializable

@Serializable
data class CursorPagingResponse<T>(
    val list: List<T>,
    val cursorId: Long?,
    val hasNext: Boolean
)