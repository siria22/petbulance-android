package com.petbulance.domain.repository.feature.hospital

import com.petbulance.domain.model.feature.hospital.recent.RecentSearchKeyword
import com.petbulance.domain.model.feature.hospital.recent.ViewedHospital
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getSearchHistoryStream(): Flow<List<RecentSearchKeyword>>
    suspend fun syncSearchHistory()
    suspend fun addSearchKeyword(keyword: String)
    suspend fun deleteSearchKeyword(keyword: String)

    fun getViewedHospitalsStream(): Flow<List<ViewedHospital>>
    suspend fun syncViewedHospitals()
    suspend fun addViewedHospital(hospitalId: Long, hospitalName: String)
    suspend fun deleteViewedHospital(hospitalId: Long)
}