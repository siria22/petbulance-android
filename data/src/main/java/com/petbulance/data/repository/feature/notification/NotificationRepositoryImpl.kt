package com.petbulance.data.repository.feature.notification

import com.petbulance.data.datasource.remote.network.feature.notification.NotificationApi
import com.petbulance.data.datasource.remote.network.feature.notification.dto.DeleteAllNotificationResDto
import com.petbulance.data.datasource.remote.network.feature.notification.dto.PagingNotificationListResDto
import com.petbulance.data.datasource.remote.network.feature.notification.dto.ReadAllNotificationResDto
import com.petbulance.data.datasource.remote.network.common.safeApiCall
import com.petbulance.data.mapper.feature.notification.toDomain
import com.petbulance.domain.model.feature.notification.DeleteAllResult
import com.petbulance.domain.model.feature.notification.PagingNotificationList
import com.petbulance.domain.model.feature.notification.ReadAllResult
import com.petbulance.domain.repository.feature.notification.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val api: NotificationApi
) : NotificationRepository {

    override suspend fun getNotifications(
        lastNotificationId: Long?,
        pageSize: Int
    ): Result<PagingNotificationList> {
        return safeApiCall<PagingNotificationListResDto>(path = "/notifications") {
            api.getNotifications(lastNotificationId, pageSize)
        }.map { it.toDomain() }
    }

    override suspend fun readAllNotifications(): Result<ReadAllResult> {
        return safeApiCall<ReadAllNotificationResDto>(path = "/notifications/read-all") {
            api.readAllNotifications()
        }.map { it.toDomain() }
    }

    override suspend fun deleteAllNotifications(): Result<DeleteAllResult> {
        return safeApiCall<DeleteAllNotificationResDto>(path = "/notifications") {
            api.deleteAllNotifications()
        }.map { it.toDomain() }
    }
}
