package com.petbulance.data.datasource.remote.network.feature.support.qna.dto

import kotlinx.serialization.Serializable

@Serializable
data class QnaDetailResDto(
    val qnaId: Long,
    val title: String,
    val content: String,
    val createdAt: String,
    val status: String,
    val answer: QnaAnswerDto? = null
)

@Serializable
data class QnaAnswerDto(
    val content: String,
    val answeredAt: String
)
