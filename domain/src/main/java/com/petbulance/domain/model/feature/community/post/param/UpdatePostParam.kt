package com.petbulance.domain.model.feature.community.post.param

data class UpdatePostParam(
    val topic: String,
    val title: String,
    val content: String,
    val imagesToKeepOrAdd: List<ImageUpdateParam> = emptyList(),
    val imageUrlsToDelete: List<String> = emptyList()
)

data class ImageUpdateParam(
    val imageUrl: String,
    val imageOrder: Int,
    val isThumbnail: Boolean
)