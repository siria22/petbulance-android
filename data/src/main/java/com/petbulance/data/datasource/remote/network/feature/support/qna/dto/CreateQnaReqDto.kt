package com.petbulance.data.datasource.remote.network.feature.support.qna.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateQnaReqDto(
    val title: String,
    val content: String
)