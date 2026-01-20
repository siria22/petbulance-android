package com.example.data.datasource.remote.network.feature.community.post.dto.post

import kotlinx.serialization.Serializable

@Serializable
data class PagingPostSearchListResDto(
    val content: List<PostSearchListResDto>,
    val hasNext: Boolean,
    val totalPostCount: Long
)

@Serializable
data class PostSearchListResDto(
    val postId: Long,
    val boardId: Long,
    val boardName: String,
    val category: List<String>,
    val writerProfileUrl: String? = null,
    val writerNickname: String,
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