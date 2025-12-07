package com.example.data.datasource.remote.network.feature.support.qna.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateQnaResDto(
    val qnaId: Long,
    val title: String,
    val content: String,
    val createdAt: String // Server: LocalDateTime
)