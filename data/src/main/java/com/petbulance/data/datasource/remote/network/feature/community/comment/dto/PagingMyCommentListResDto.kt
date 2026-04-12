package com.petbulance.data.datasource.remote.network.feature.community.comment.dto

import kotlinx.serialization.Serializable

@Serializable
data class PagingMyCommentListResDto(
    val content: List<MyCommentListResDto>,
    val lastCommentId: Long? = null,
    val hasNext: Boolean,
)

@Serializable
data class MyCommentListResDto(
    val commentId: Long,
    val boardId: Long? = null,
    val postId: Long,
    val postTitle: String,
    val commentContent: String,
    val createdAt: String,
    val hidden: Boolean,
    val secret: Boolean = false,
)
