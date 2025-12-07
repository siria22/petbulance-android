package com.example.domain.model.feature.community.post

data class PagingPostList(
    val items: List<PostSummary>,
    val hasNext: Boolean
)

data class PostSummary(
    val id: Long,
    val title: String,
    val summary: String,
    val thumbnailUrl: String?,
    val commentCount: Int,
    val likeCount: Int
)