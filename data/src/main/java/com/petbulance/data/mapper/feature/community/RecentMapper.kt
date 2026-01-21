package com.petbulance.data.mapper.feature.community

import com.petbulance.data.datasource.remote.network.feature.community.recent.dto.RecentCommunityResDto
import com.petbulance.domain.model.feature.community.recent.RecentCommunityKeyword

fun RecentCommunityResDto.toDomain() = RecentCommunityKeyword(
    id = keywordId,
    keyword = keyword,
    date = createdAt
)