package com.petbulance.domain.model.feature.community.post

import java.time.LocalDateTime

data class Post(
    val postId: Long,
    val type: String,
    val topic: String,
    val title: String,
    val content: String,
    val imageUrls: List<String>,
    val updatedAt: LocalDateTime
)