package com.petbulance.domain.usecase.feature.notification

import com.petbulance.domain.model.feature.notification.ReadAllResult
import com.petbulance.domain.repository.feature.notification.NotificationRepository
import javax.inject.Inject

class ReadAllNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(): Result<ReadAllResult> {
        return repository.readAllNotifications()
    }
}
