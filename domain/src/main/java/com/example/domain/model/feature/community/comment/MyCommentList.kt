package com.example.domain.model.feature.community.comment

data class MyCommentList(
    val content: List<MyCommentListRes>,
    val hasNext: Boolean,
)

data class MyCommentListRes(
    val commentId: Long,
    val boardId: Long,
    val postId: Long,
    val postTitle: String,
    val commentContent: String,
    val hidden: Boolean
)
