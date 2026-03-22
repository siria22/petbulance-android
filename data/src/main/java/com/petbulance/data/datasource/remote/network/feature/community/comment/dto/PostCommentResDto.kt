package com.petbulance.data.datasource.remote.network.feature.community.comment.dto

import com.petbulance.data.util.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class PostCommentResDto(
    val commentId: Long,
    val content: String,
    val parentId: Long?,
    val mentionUserNickname: String?,
    @SerialName("secret")
    val isSecret: Boolean,
    val imageUrl: String?,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime
)