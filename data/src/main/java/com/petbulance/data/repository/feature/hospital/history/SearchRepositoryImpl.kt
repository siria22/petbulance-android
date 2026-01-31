package com.petbulance.data.repository.feature.hospital.search

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.petbulance.data.datasource.local.database.dao.SearchDao
import com.petbulance.data.datasource.local.database.dao.ViewedHospitalDao
import com.petbulance.data.datasource.local.database.entity.SearchHistoryEntity
import com.petbulance.data.datasource.local.database.entity.ViewedHospitalEntity
import com.petbulance.data.datasource.remote.network.common.safeApiCall
import com.petbulance.data.datasource.remote.network.feature.hospital.history.HistoryApi
import com.petbulance.data.datasource.remote.network.feature.hospital.history.dto.RecentHospitalResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.history.dto.ViewedHospitalResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.history.dto.ViewedHospitalSaveResDto
import com.petbulance.data.worker.SyncSearchWorker
import com.petbulance.domain.model.feature.hospital.recent.RecentSearchKeyword
import com.petbulance.domain.model.feature.hospital.recent.ViewedHospital
import com.petbulance.domain.repository.feature.hospital.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchDao: SearchDao,
    private val viewedHospitalDao: ViewedHospitalDao,
    private val historyApi: HistoryApi,
    private val workManager: WorkManager
) : SearchRepository {

    override fun getSearchHistoryStream(): Flow<List<RecentSearchKeyword>> {
        return searchDao.getSearchHistoryStream().map { entities ->
            entities.map { entity ->
                RecentSearchKeyword(
                    id = entity.serverId ?: 0,
                    keyword = entity.keyword,
                    date = convertTimestampToString(entity.timestamp)
                )
            }
        }
    }

    override suspend fun syncSearchHistory() {
        safeApiCall<List<RecentHospitalResDto>>(path = "/recents/hospitals") {
            historyApi.getRecentKeywords()
        }.onSuccess { remoteList ->
            val entities = remoteList.map { dto ->
                SearchHistoryEntity(
                    serverId = dto.keywordId,
                    keyword = dto.keyword,
                    timestamp = System.currentTimeMillis(),
                    isSynced = true
                )
            }
            searchDao.insertAll(entities)
        }.onFailure {
            it.printStackTrace()
        }
    }

    override suspend fun addSearchKeyword(keyword: String) {
        val existingEntity = searchDao.findByKeyword(keyword)
        if (existingEntity != null) {
            searchDao.deleteByKeyword(keyword)
        }

        val entity = SearchHistoryEntity(
            keyword = keyword,
            timestamp = System.currentTimeMillis(),
            isSynced = false
        )
        searchDao.insertOrUpdate(entity)
    }

    override suspend fun deleteSearchKeyword(keyword: String) {
        val entityToDelete = searchDao.findByKeyword(keyword)
        searchDao.deleteByKeyword(keyword)

        entityToDelete?.serverId?.let { id ->
            safeApiCall<Unit>(path = "/recents/hospitals/$id") {
                historyApi.deleteRecentKeyword(id)
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    private fun enqueueSyncWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val syncRequest = OneTimeWorkRequestBuilder<SyncSearchWorker>().build()
        workManager.enqueueUniqueWork("SyncSearchWork", ExistingWorkPolicy.KEEP, syncRequest)
    }

    private fun convertTimestampToString(timestamp: Long): String {
        return LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(timestamp),
            ZoneId.systemDefault()
        ).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }


    override fun getViewedHospitalsStream(): Flow<List<ViewedHospital>> {
        return viewedHospitalDao.getViewedHospitalStream().map { entities ->
            entities.map { entity ->
                ViewedHospital(
                    hospitalId = entity.hospitalId,
                    hospitalName = entity.hospitalName,
                    viewedAt = convertTimestampToString(entity.timestamp)
                )
            }
        }
    }

    override suspend fun syncViewedHospitals() {
        safeApiCall<ViewedHospitalResDto>(path = "/recents/viewed") {
            historyApi.getViewedHospitals()
        }.onSuccess { remoteList ->
            val entities = remoteList.viewedHospitals.map { dto ->
                ViewedHospitalEntity(
                    hospitalId = dto.hospitalId,
                    hospitalName = dto.name,
                    timestamp = System.currentTimeMillis(), // 서버 Timestamp가 없다면 현재 시간 사용
                    isSynced = true
                )
            }
            viewedHospitalDao.insertAll(entities)
        }.onFailure {
            it.printStackTrace()
        }
    }

    override suspend fun addViewedHospital(hospitalId: Long, hospitalName: String) {
        val entity = ViewedHospitalEntity(
            hospitalId = hospitalId,
            hospitalName = hospitalName,
            timestamp = System.currentTimeMillis(),
            isSynced = false
        )
        viewedHospitalDao.insertOrUpdate(entity)

        safeApiCall<ViewedHospitalSaveResDto>(path = "/recents/viewed") {
            historyApi.saveViewedHospital(hospitalId)
        }.onSuccess {
            viewedHospitalDao.markAsSynced(hospitalId)
        }.onFailure {
            // enqueueSyncWorker() // TODO: ViewedHospital용 SyncWorker 필요 시 구현
        }
    }

    override suspend fun deleteViewedHospital(hospitalId: Long) {
        viewedHospitalDao.deleteByHospitalId(hospitalId)
        safeApiCall<Unit>(path = "/recents/viewed/$hospitalId") {
            historyApi.deleteViewedHospital(hospitalId)
        }.onFailure {
            it.printStackTrace()
        }
    }

}