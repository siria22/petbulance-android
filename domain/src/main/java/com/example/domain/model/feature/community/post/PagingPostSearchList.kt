package com.example.domain.model.feature.community.post

data class PagingPostSearchList(
    val items: List<PostSearchSummary>,
    val hasNext: Boolean,
    val totalPostCount: Long
)

data class PostSearchSummary(
    val id: Long,
    val title: String,
    val contentSnippet: String,
    val boardName: String,
    val createdAt: String
)