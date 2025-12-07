package com.example.domain.model.feature.community.post

import java.time.LocalDateTime

data class Post(
    val postId: Long,
    val boardId: Long,
    val category: String,
    val title: String,
    val content: String,
    val imageUrls: List<String>,
    val updatedAt: LocalDateTime
)