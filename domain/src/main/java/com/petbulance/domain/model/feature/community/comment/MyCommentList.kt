package com.petbulance.domain.model.feature.community.comment

data class MyCommentList(
    val items: List<MyCommentListRes>,
    val hasNext: Boolean,
)

data class MyCommentListRes(
    val commentId: Long,
    val boardId: Long? = null,
    val postId: Long,
    val postTitle: String,
    val commentContent: String,
    val createdAt: String,
    val hidden: Boolean
)
