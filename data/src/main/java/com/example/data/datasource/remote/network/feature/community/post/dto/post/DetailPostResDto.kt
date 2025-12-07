package com.example.data.datasource.remote.network.feature.community.post.dto.post

import kotlinx.serialization.Serializable

@Serializable
data class DetailPostResDto(
    val board: BoardInfo,
    val post: PostInfo
)

@Serializable
data class BoardInfo(
    val boardId: Long,
    val boardName: String,
    val category: String
)

@Serializable
data class PostInfo(
    val postId: Long,
    val title: String,
    val writerNickname: String?,
    val writerProfileUrl: String?,
    val createdAt: String,
    val content: String,
    val images: List<ImageInfo> = emptyList(),
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val viewCount: Int = 0,
    val likedByUser: Boolean = false,
    val isCurrentUserPost: Boolean = false
)

@Serializable
data class ImageInfo(
    val imageUrl: String,
    val imageOrder: Int = 0,
    val thumbnail: Boolean = false
)