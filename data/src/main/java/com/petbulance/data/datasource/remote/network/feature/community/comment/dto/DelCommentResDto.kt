package com.petbulance.data.datasource.remote.network.feature.community.comment.dto

import kotlinx.serialization.Serializable

@Serializable
data class DelCommentResDto(
    val message: String
)