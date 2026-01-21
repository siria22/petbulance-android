package com.petbulance.data.datasource.remote.network.feature.community.post.dto.comment

import kotlinx.serialization.Serializable

@Serializable
data class PostCommentResDto(
    val commentId: Long,
    val content: String,
    val parentId: Long? = null,
    val mentionUserNickname: String? = null,
    val isSecret: Boolean,
    val imageUrl: String? = null,
    val createdAt: String
)