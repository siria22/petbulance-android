package com.petbulance.data.repository.feature.community.recent

import com.petbulance.data.datasource.remote.network.common.safeApiCall
import com.petbulance.data.datasource.remote.network.feature.community.recent.RecentApi
import com.petbulance.data.datasource.remote.network.feature.community.recent.dto.DeleteKeywordResDto
import com.petbulance.data.datasource.remote.network.feature.community.recent.dto.RecentCommunityResDto
import com.petbulance.data.mapper.feature.community.toDomain
import com.petbulance.domain.model.feature.community.recent.RecentCommunityKeyword
import com.petbulance.domain.repository.feature.community.RecentRepository
import javax.inject.Inject

class RecentRepositoryImpl @Inject constructor(
    private val api: RecentApi
) : RecentRepository {
    override suspend fun getRecentCommunityKeywords(): Result<List<RecentCommunityKeyword>> {
        return safeApiCall<List<RecentCommunityResDto>>(path = "/recents/community") {
            api.getRecentCommunityKeywords()
        }.map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun deleteRecentCommunityKeyword(keywordId: String): Result<String> {
        return safeApiCall<DeleteKeywordResDto>(path = "/recents/community/$keywordId") {
            api.deleteRecentCommunityKeyword(keywordId)
        }.map { dto ->
            if (dto.success) {
                dto.message
            } else {
                throw Exception(dto.message)
            }
        }
    }
}