package com.petbulance.data.repository.feature.hospital.history

import com.petbulance.data.repository.MockFixtures
import com.petbulance.domain.model.feature.hospital.recent.RecentSearchKeyword
import com.petbulance.domain.model.feature.hospital.recent.ViewedHospital
import com.petbulance.domain.model.feature.hospital.recent.ViewedHospitalList
import com.petbulance.domain.repository.feature.hospital.HistoryRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class MockHistoryRepository @Inject constructor() : HistoryRepository {

    private val recentKeywords = mutableListOf(
        RecentSearchKeyword(1, "앵무새 깃털", LocalDateTime.now().minusDays(2).format(DateTimeFormatter.ISO_LOCAL_DATE)),
        RecentSearchKeyword(2, "24시 특수동물병원", LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE)),
        RecentSearchKeyword(3, "토끼 부정교합", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE))
    )

    private val viewedHospitals = mutableListOf(
        ViewedHospital(2, "하늘깃 조류 동물병원", LocalDateTime.now().minusHours(2).withNano(0).toString()),
        ViewedHospital(4, "달빛 24시 특수동물 메디컬센터", LocalDateTime.now().minusHours(1).withNano(0).toString()),
        ViewedHospital(5, "솜털 토끼 클리닉", LocalDateTime.now().withNano(0).toString())
    )

    override suspend fun getRecentKeywords(): Result<List<RecentSearchKeyword>> {
        return Result.success(recentKeywords.reversed())
    }

    override suspend fun saveRecentKeyword(keyword: String): Result<RecentSearchKeyword> {
        val newKeyword = RecentSearchKeyword(
            id = (recentKeywords.maxOfOrNull { it.id } ?: 0) + 1,
            keyword = keyword,
            date = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        )
        recentKeywords.add(newKeyword)
        return Result.success(newKeyword)
    }

    override suspend fun deleteRecentKeyword(keywordId: Long): Result<Unit> {
        recentKeywords.removeIf { it.id == keywordId }
        return Result.success(Unit)
    }

    override suspend fun saveViewedHospital(hospitalId: Long): Result<Unit> {
        val newViewed = ViewedHospital(
            hospitalId = hospitalId,
            hospitalName = MockFixtures.findHospital(hospitalId)?.name.orEmpty(),
            viewedAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
        viewedHospitals.removeIf { it.hospitalId == hospitalId } // Remove old one if exists
        viewedHospitals.add(newViewed)
        return Result.success(Unit)
    }

    override suspend fun getViewedHospitals(): Result<ViewedHospitalList> {
        val list = viewedHospitals.sortedByDescending { it.viewedAt }
        return Result.success(ViewedHospitalList(items = list, totalCount = list.size.toLong()))
    }

    override suspend fun deleteViewedHospital(hospitalId: Long): Result<Unit> {
        viewedHospitals.removeIf { it.hospitalId == hospitalId }
        return Result.success(Unit)
    }
}