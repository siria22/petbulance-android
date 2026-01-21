package com.petbulance.data.datasource.remote.network.feature.community.post.dto.post

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePostResDto(
    val postId: Long,
    val boardId: Long,
    val category: String,
    val title: String,
    val content: String,
    val imageUrls: List<String>,
    val updatedAt: String? = null
)