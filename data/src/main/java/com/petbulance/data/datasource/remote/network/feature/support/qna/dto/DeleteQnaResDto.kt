package com.petbulance.data.datasource.remote.network.feature.support.qna.dto

import kotlinx.serialization.Serializable

@Serializable
data class DeleteQnaResDto(
    val qnaId: Long,
    val message: String
)