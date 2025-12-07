package com.example.domain.model.feature.community.post.param

data class CreateCommentParam(
    val content: String,
    val parentId: Long? = null,
    val mentionUserNickname: String? = null,
    val imageUrl: String? = null,
    val isSecret: Boolean = false
)