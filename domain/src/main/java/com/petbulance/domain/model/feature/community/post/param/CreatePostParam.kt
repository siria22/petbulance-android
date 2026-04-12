package com.petbulance.domain.model.feature.community.post.param

data class CreatePostParam(
    val type: String,
    val topic: String,
    val title: String,
    val content: String,
    val imageUrls: List<String> = emptyList()
)