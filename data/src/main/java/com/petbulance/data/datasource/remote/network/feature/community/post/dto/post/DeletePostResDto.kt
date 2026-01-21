package com.petbulance.data.datasource.remote.network.feature.community.post.dto.post

import kotlinx.serialization.Serializable

@Serializable
data class DeletePostResDto(
    val postId: Long,
    val boardId: Long,
    val deleted: Boolean,
    val hidden: Boolean,
    val deletedAt: String? = null
)