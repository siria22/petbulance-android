package com.petbulance.data.datasource.remote.network.feature.community.comment.dto

data class PagingMyCommentListResDto(
    val content: List<MyCommentListResDto>,
    val hasNext: Boolean,
)

data class MyCommentListResDto(
    val commentId: Long,
    val boardId: Long,
    val postId: Long,
    val postTitle: String,
    val commentContent: String,
    val createdAt: String,
    val hidden: Boolean,
)