package com.petbulance.data.repository.feature.hospital.history

import com.petbulance.domain.model.feature.hospital.recent.RecentSearchKeyword
import com.petbulance.domain.model.feature.hospital.recent.ViewedHospital
import com.petbulance.domain.repository.feature.hospital.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class MockSearchRepository @Inject constructor() : SearchRepository {
    override fun getSearchHistoryStream(): Flow<List<RecentSearchKeyword>> =
        flowOf(listOf(RecentSearchKeyword(1, "강아지 예방접종", "2024-01-21")))
    override suspend fun syncSearchHistory() {}
    override suspend fun addSearchKeyword(keyword: String) {}
    override suspend fun deleteSearchKeyword(keyword: String) {}

    override fun getViewedHospitalsStream(): Flow<List<ViewedHospital>> = flowOf(emptyList())
    override suspend fun syncViewedHospitals() {}
    override suspend fun addViewedHospital(hospitalId: Long, hospitalName: String) {}
    override suspend fun deleteViewedHospital(hospitalId: Long) {}
}