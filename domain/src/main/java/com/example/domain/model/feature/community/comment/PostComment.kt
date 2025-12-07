package com.example.domain.model.feature.community.comment

import java.time.LocalDateTime

data class PostComment(
    val commentId: Long,
    val content: String,
    val parentId: Long?,
    val mentionUserNickname: String?,
    val isSecret: Boolean,
    val imageUrl: String?,
    val createdAt: LocalDateTime
)