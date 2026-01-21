package com.petbulance.data.datasource.remote.network.feature.community.comment.dto

import kotlinx.serialization.Serializable

@Serializable
data class SearchPostCommentListResDto(
    val content: List<SearchPostCommentResDto> = emptyList(),
    val hasNext: Boolean = false,
    val totalCount: Long = 0L
)

@Serializable
data class SearchPostCommentResDto(
    val commentId: Long = 0L,
    val boardId: Long = 0L,
    val boardName: String = "",
    val postId: Long = 0L,
    val postTitle: String = "",
    val writerNickname: String = "",
    val commentContent: String = "",
    val createdAt: String = ""
)