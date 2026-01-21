package com.petbulance.domain.repository.feature.hospital

import com.petbulance.domain.model.feature.hospital.recent.RecentSearchKeyword
import com.petbulance.domain.model.feature.hospital.recent.ViewedHospitalList

interface HistoryRepository {
    suspend fun getRecentKeywords(): Result<List<RecentSearchKeyword>>
    suspend fun saveRecentKeyword(keyword: String): Result<RecentSearchKeyword>
    suspend fun deleteRecentKeyword(keywordId: Long): Result<Unit>
    suspend fun saveViewedHospital(hospitalId: Long): Result<Unit>
    suspend fun getViewedHospitals(): Result<ViewedHospitalList>
    suspend fun deleteViewedHospital(hospitalId: Long): Result<Unit>
}