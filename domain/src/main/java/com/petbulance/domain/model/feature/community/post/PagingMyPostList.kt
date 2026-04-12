package com.petbulance.domain.model.feature.community.post

data class PagingMyPostList(
    val items: List<MyPostSummary>,
    val hasNext: Boolean
)

data class MyPostSummary(
    val postId: Long,
    val title: String,
    val content: String,
    val createdAt: String,
    val viewCount: Long,
    val likeCount: Long?,
    val thumbnailUrl: String?,
    val hidden: Boolean
)
