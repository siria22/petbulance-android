package com.petbulance.domain.model.feature.community.post

data class PagingPostList(
    val noticeBanner: NoticeBanner? = null,
    val items: List<PostSummary>,
    val hasNext: Boolean
)

data class PostSummary(
    val id: Long,
    val type: String,
    val topic: String,
    val title: String,
    val content: String,
    val thumbnailUrl: String?,
    val imageCount: Int,
    val viewCount: Int,
    val commentCount: Int,
    val likeCount: Int,
    val createdAt: String,
    val isLiked: Boolean
)