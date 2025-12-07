package com.example.data.repository.feature.hospital.history

import com.example.domain.model.feature.hospital.recent.RecentSearchKeyword
import com.example.domain.model.feature.hospital.recent.ViewedHospital
import com.example.domain.model.feature.hospital.recent.ViewedHospitalList
import com.example.domain.repository.feature.hospital.HistoryRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class MockHistoryRepository @Inject constructor() : HistoryRepository {

    private val recentKeywords = mutableListOf(
        RecentSearchKeyword(1, "강아지 슬개골", "2025-12-10"),
        RecentSearchKeyword(2, "고양이 신부전", "2025-12-09")
    )

    private val viewedHospitals = mutableListOf(
        ViewedHospital(1, "행복 동물병원", "2025-12-11T10:00:00"),
        ViewedHospital(2, "튼튼 동물병원", "2025-12-11T11:00:00")
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
            hospitalName = "새로 본 병원 $hospitalId",
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