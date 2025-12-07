package com.example.domain.model.feature.community.comment

data class UpdatePostCommentReq(
    val content: String,
    val imageUrl: String?,
    val isSecret: Boolean
)