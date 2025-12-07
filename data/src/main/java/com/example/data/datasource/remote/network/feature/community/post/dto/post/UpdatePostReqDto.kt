package com.example.data.datasource.remote.network.feature.community.post.dto.post

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePostReqDto(
    val category: String,
    val title: String,
    val content: String,
    val imagesToKeepOrAdd: List<ImageUpdateDto> = emptyList(),
    val imageUrlsToDelete: List<String> = emptyList()
)

@Serializable
data class ImageUpdateDto(
    val imageUrl: String,
    val imageOrder: Int,
    val thumbnail: Boolean
)