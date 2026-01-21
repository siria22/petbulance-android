package com.petbulance.data.datasource.remote.network.feature.community.post.dto.like

import kotlinx.serialization.Serializable

@Serializable
data class PostLikeDto(
    val postId: Long,
    val likeCount: Long,
    val liked: Boolean
)