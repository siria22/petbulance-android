package com.example.domain.model.feature.community.post.param

data class CreatePostParam(
    val boardId: Long,
    val category: String,
    val title: String,
    val content: String,
    val imageUrls: List<String> = emptyList()
)