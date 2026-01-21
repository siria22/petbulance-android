package com.petbulance.domain.model.feature.community.post

data class PostLike(
    val postId: Long,
    val currentLikeCount: Long,
    val isLiked: Boolean
)