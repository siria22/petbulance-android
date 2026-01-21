package com.petbulance.data.datasource.remote.network.feature.community.comment.dto

import com.petbulance.data.util.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class PostCommentResDto(
    val commentId: Long,
    val content: String,
    val parentId: Long?,              // 부모 댓글 없으면 null
    val mentionUserNickname: String?, // 멘션 대상 없으면 null
    val isSecret: Boolean,
    val imageUrl: String?,            // 이미지 없으면 null 가능성
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime
)