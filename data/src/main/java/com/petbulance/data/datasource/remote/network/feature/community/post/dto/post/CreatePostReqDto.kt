package com.petbulance.data.datasource.remote.network.feature.community.post.dto.post

import kotlinx.serialization.Serializable

@Serializable
data class CreatePostReqDto(
    val type: String,
    val topic: String,
    val title: String,
    val content: String,
    val imageUrls: List<String> = emptyList()
)
