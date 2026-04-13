package com.petbulance.domain.usecase.feature.notification

import com.petbulance.domain.model.feature.notification.PagingNotificationList
import com.petbulance.domain.repository.feature.notification.NotificationRepository
import javax.inject.Inject

class GetNotificationListUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(
        lastNotificationId: Long? = null,
        pageSize: Int = 20
    ): Result<PagingNotificationList> {
        return repository.getNotifications(lastNotificationId, pageSize)
    }
}
