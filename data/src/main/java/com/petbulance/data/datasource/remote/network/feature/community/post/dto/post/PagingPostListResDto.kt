package com.petbulance.data.datasource.remote.network.feature.community.post.dto.post

import kotlinx.serialization.Serializable

@Serializable
data class PagingPostListResDto(
    val noticeBanner: NoticeBannerInfoDto? = null,
    val lastPostId: Long? = null,
    val content: List<PostListResDto>,
    val hasNext: Boolean
)

@Serializable
data class PostListResDto(
    val postId: Long,
    val type: String,
    val topic: String,
    val createdAt: String,
    val thumbnailUrl: String? = null,
    val imageCount: Long,
    val title: String,
    val content: String,
    val likeCount: Long,
    val commentCount: Long,
    val viewCount: Long,
    val likedByUser: Boolean
)