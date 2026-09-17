package com.petbulance.data.repository.feature.community.recent

import com.petbulance.domain.model.feature.community.recent.RecentCommunityKeyword
import com.petbulance.domain.repository.feature.community.RecentRepository
import javax.inject.Inject

class MockRecentRepository @Inject constructor() : RecentRepository {

    private val recentKeywords = mutableListOf(
        RecentCommunityKeyword("1", "게코 은신처", "2026-09-15"),
        RecentCommunityKeyword("2", "햄스터 케이지", "2026-09-16")
    )

    override suspend fun getRecentCommunityKeywords(): Result<List<RecentCommunityKeyword>> {
        return Result.success(recentKeywords.reversed())
    }

    override suspend fun deleteRecentCommunityKeyword(keywordId: String): Result<String> {
        val removed = recentKeywords.removeIf { it.id == keywordId }
        return if (removed) {
            Result.success("삭제되었습니다.")
        } else {
            Result.failure(Exception("해당 키워드를 찾을 수 없습니다."))
        }
    }
}