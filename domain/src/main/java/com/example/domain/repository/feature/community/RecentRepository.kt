package com.example.domain.repository.feature.community

import com.example.domain.model.feature.community.recent.RecentCommunityKeyword

interface RecentRepository {
    suspend fun getRecentCommunityKeywords(): Result<List<RecentCommunityKeyword>>
    suspend fun deleteRecentCommunityKeyword(keywordId: String): Result<String>
}