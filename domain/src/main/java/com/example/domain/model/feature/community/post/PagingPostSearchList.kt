package com.example.domain.model.feature.community.post

data class PagingPostSearchList(
    val items: List<PostSearchSummary>,
    val hasNext: Boolean,
    val totalPostCount: Long
)

data class PostSearchSummary(
    val id: Long,
    val boardId: Long,
    val boardName: String,
    val categories: List<String>,
    val title: String,
    val content: String,
    val thumbnailUrl: String?,
    val imageCount: Int,
    val viewCount: Int,
    val commentCount: Int,
    val likeCount: Int,
    val createdAt: String,
    val writerNickname: String,
    val writerProfileUrl: String?,
    val isLiked: Boolean
)