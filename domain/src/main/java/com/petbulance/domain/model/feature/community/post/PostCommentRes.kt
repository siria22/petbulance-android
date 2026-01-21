package com.petbulance.domain.model.feature.community.post

data class PostCommentRes(
    val commentId: Long,
    val content: String,
    val parentId: Long? = null,
    val mentionUserNickname: String? = null,
    val isSecret: Boolean,
    val imageUrl: String? = null,
    val createdAt: String
)