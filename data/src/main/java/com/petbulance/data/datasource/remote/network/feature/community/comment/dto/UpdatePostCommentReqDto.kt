package com.petbulance.data.datasource.remote.network.feature.community.comment.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePostCommentReqDto(
    val content: String,
    val imageUrl: String?,
    val isSecret: Boolean
)