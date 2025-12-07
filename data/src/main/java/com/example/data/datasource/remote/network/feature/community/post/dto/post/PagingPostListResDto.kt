package com.example.data.datasource.remote.network.feature.community.post.dto.post

import kotlinx.serialization.Serializable

@Serializable
data class PagingPostListResDto(
    val content: List<PostListResDto>,
    val hasNext: Boolean
)

@Serializable
data class PostListResDto(
    val postId: Long,
    val title: String,
    val summary: String?,
    val imageUrl: String?,
    val commentCount: Int,
    val likeCount: Int
)