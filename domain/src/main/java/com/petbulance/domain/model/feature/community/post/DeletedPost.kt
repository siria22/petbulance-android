package com.petbulance.domain.model.feature.community.post

data class DeletedPost(
    val postId: Long,
    val boardId: Long? = null,
    val deleted: Boolean,
    val hidden: Boolean,
    val deletedAt: String? = null
)