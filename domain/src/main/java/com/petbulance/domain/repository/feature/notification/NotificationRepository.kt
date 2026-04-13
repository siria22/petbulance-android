package com.petbulance.domain.repository.feature.notification

import com.petbulance.domain.model.feature.notification.DeleteAllResult
import com.petbulance.domain.model.feature.notification.PagingNotificationList
import com.petbulance.domain.model.feature.notification.ReadAllResult

interface NotificationRepository {
    suspend fun getNotifications(
        lastNotificationId: Long? = null,
        pageSize: Int = 20
    ): Result<PagingNotificationList>

    suspend fun readAllNotifications(): Result<ReadAllResult>
    suspend fun deleteAllNotifications(): Result<DeleteAllResult>
}
