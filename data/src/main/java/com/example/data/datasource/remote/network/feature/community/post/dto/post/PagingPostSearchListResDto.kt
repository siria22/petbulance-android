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
    val title: String,
    val contentSnippet: String?,
    val boardName: String,
    val createdAt: String
)