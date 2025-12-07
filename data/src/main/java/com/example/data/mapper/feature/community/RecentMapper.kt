package com.example.data.mapper.feature.community

import com.example.data.datasource.remote.network.feature.community.recent.dto.RecentCommunityResDto
import com.example.domain.model.feature.community.recent.RecentCommunityKeyword

fun RecentCommunityResDto.toDomain() = RecentCommunityKeyword(
    id = keywordId,
    keyword = keyword,
    date = createdAt
)