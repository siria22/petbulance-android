package com.petbulance.data.datasource.remote.network.feature.community.post.dto.post

import kotlinx.serialization.Serializable

@Serializable
data class PagingMyPostListResDto(
    val content: List<MyPostListResDto>,
    val lastPostId: Long? = null,
    val hasNext: Boolean
)

@Serializable
data class MyPostListResDto(
    val postId: Long,
    val title: String,
    val content: String,
    val createdAt: String,
    val viewCount: Long,
    val likeCount: Long? = null,
    val thumbnailUrl: String? = null,
    val hidden: Boolean
)
