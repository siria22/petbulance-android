package com.example.data.datasource.remote.network.feature.support.qna.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateQnaResDto(
    val qnaId: Long,
    val title: String,
    val content: String,
    val updatedAt: String
)
