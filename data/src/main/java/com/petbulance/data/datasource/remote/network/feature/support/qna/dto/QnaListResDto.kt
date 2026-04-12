package com.petbulance.data.datasource.remote.network.feature.support.qna.dto

import kotlinx.serialization.Serializable

@Serializable
data class PagingQnaListResDto(
    val content: List<QnaListItemResDto>,
    val hasNext: Boolean
)

@Serializable
data class QnaListItemResDto(
    val qnaId: Long,
    val title: String,
    val content: String,
    val createdAt: String,
    val status: String
)
