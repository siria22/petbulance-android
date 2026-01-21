package com.petbulance.data.datasource.remote.network.feature.community.post.dto.post

import kotlinx.serialization.Serializable

@Serializable
data class PagingPostListResDto(
    val content: List<PostListResDto>,
    val hasNext: Boolean
)

@Serializable
data class PostListResDto(
    val postId: Long,
    val boardId: Long,
    val boardName: String,
    val category: String,
    val createdAt: String,
    val thumbnailUrl: String? = null,
    val imageCount: Int,
    val title: String,
    val content: String,
    val likeCount: Int,
    val commentCount: Int,
    val viewCount: Int,
    val likedByUser: Boolean
)