package com.petbulance.domain.usecase.feature.notification

import com.petbulance.domain.model.feature.notification.DeleteAllResult
import com.petbulance.domain.repository.feature.notification.NotificationRepository
import javax.inject.Inject

class DeleteAllNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(): Result<DeleteAllResult> {
        return repository.deleteAllNotifications()
    }
}
