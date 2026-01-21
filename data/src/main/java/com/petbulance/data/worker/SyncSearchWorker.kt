package com.petbulance.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.petbulance.data.datasource.local.database.dao.SearchDao
import com.petbulance.data.datasource.remote.network.feature.hospital.history.HistoryApi
import com.petbulance.data.datasource.remote.network.feature.hospital.history.dto.RecentHospitalSaveResDto
import com.petbulance.domain.utils.LOGGER_TAG
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class SyncSearchWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val searchDao: SearchDao,
    private val historyApi: HistoryApi
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val unsyncedItems = searchDao.getUnsyncedItems()

            for (entity in unsyncedItems) {
                try {
                    val response = historyApi.saveRecentKeyword(entity.keyword)
                    if (response.status.isSuccess()) {
                        val body = response.body<RecentHospitalSaveResDto>()
                        searchDao.markAsSynced(entity.keyword, body.keywordId)
                    } else {
                        return@withContext Result.retry()
                    }
                } catch (e: Exception) {
                    return@withContext Result.retry()
                }
            }
            Result.success()
        } catch (e: Exception) {
            Log.d(LOGGER_TAG, "SyncSearchWorker.doWork() failed: ${e.message}")
            Result.retry()
        }
    }
}