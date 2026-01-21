package com.petbulance.data.repository.feature.hospital.history

import com.petbulance.data.datasource.remote.network.common.safeApiCall
import com.petbulance.data.datasource.remote.network.feature.hospital.history.HistoryApi
import com.petbulance.data.datasource.remote.network.feature.hospital.history.dto.RecentHospitalResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.history.dto.RecentHospitalSaveResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.history.dto.ViewedHospitalResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.history.dto.ViewedHospitalSaveResDto
import com.petbulance.data.mapper.feature.hospital.toDomain
import com.petbulance.domain.model.feature.hospital.recent.RecentSearchKeyword
import com.petbulance.domain.model.feature.hospital.recent.ViewedHospitalList
import com.petbulance.domain.repository.feature.hospital.HistoryRepository
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyApi: HistoryApi
) : HistoryRepository {

    override suspend fun getRecentKeywords(): Result<List<RecentSearchKeyword>> {
        return safeApiCall<List<RecentHospitalResDto>>(path = "/recents/hospitals") {
            historyApi.getRecentKeywords()
        }.map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun saveRecentKeyword(keyword: String): Result<RecentSearchKeyword> {
        return safeApiCall<RecentHospitalSaveResDto>(path = "/recents/hospitals") {
            historyApi.saveRecentKeyword(keyword)
        }.map { it.toDomain() }
    }

    override suspend fun deleteRecentKeyword(keywordId: Long): Result<Unit> {
        return safeApiCall<Unit>(path = "/recents/hospitals/$keywordId") {
            historyApi.deleteRecentKeyword(keywordId)
        }
    }

    override suspend fun saveViewedHospital(hospitalId: Long): Result<Unit> {
        return safeApiCall<ViewedHospitalSaveResDto>(path = "/recents/viewed") {
            historyApi.saveViewedHospital(hospitalId)
        }.map { }
    }

    override suspend fun getViewedHospitals(): Result<ViewedHospitalList> {
        return safeApiCall<ViewedHospitalResDto>(path = "/recents/viewed") {
            historyApi.getViewedHospitals()
        }.map { dto ->
            ViewedHospitalList(
                items = dto.viewedHospitals.map { it.toDomain() },
                totalCount = dto.total
            )
        }
    }

    override suspend fun deleteViewedHospital(hospitalId: Long): Result<Unit> {
        return safeApiCall<Unit>(path = "/recents/viewed/$hospitalId") {
            historyApi.deleteViewedHospital(hospitalId)
        }
    }
}