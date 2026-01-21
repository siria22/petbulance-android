package com.petbulance.domain.model.feature.community.post

data class PagingCommentList(
    val items: List<Comment>,
    val hasNext: Boolean,
    val totalCount: Long
)

data class Comment(
    val isRoot: Boolean,
    val commentId: Long,
    val parentId: Long,
    val writerInfo: WriterInfo,
    val mentionUserNickname: String,
    val content: String,
    val isSecret: Boolean,
    val isCommentFromPostAuthor: Boolean,
    val isCommentAuthor: Boolean,
    val deleted: Boolean,
    val hidden: Boolean,
    val imageUrl: String,
    val visibleToUser: Boolean,
    val createdAt: String
)