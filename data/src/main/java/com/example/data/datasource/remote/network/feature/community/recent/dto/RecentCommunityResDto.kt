package com.example.data.datasource.remote.network.feature.community.recent.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecentCommunityResDto(
    val keywordId: String, // 서버가 String으로 정의함
    val keyword: String,
    val createdAt: String  // Server: LocalDateTime
)
