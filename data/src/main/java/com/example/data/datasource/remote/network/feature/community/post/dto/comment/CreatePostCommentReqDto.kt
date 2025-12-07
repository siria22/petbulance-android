package com.example.data.datasource.remote.network.feature.community.post.dto.comment

import kotlinx.serialization.Serializable

@Serializable
data class CreatePostCommentReqDto(
    val content: String,
    val parentId: Long? = null,
    val mentionUserNickname: String? = null,
    val imageUrl: String? = null,
    val isSecret: Boolean
)