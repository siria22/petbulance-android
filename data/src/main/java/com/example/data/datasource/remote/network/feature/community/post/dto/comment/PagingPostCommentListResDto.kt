package com.example.data.datasource.remote.network.feature.community.post.dto.comment

import kotlinx.serialization.Serializable

@Serializable
data class PagingPostCommentListResDto(
    val content: List<PostCommentListResDto>,
    val hasNext: Boolean,
    val totalCommentCount: Long
)

@Serializable
data class PostCommentListResDto(
    val isRoot: Boolean,
    val commentId: Long,
    val parentId: Long,
    val writerNickname: String,
    val writerProfileUrl: String,
    val mentionUserNickname: String,
    val content: String,
    val isSecret: Boolean,
    val isCommentFromPostAuthor: Boolean,
    val isCommentAuthor: Boolean,
    val deleted: Boolean,
    val hidden: Boolean,
    val imageUrl: String,
    val visibleToUser: Boolean,
    val createdAt: String
)
