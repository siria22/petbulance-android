package com.petbulance.domain.usecase.feature.user.user

import com.petbulance.domain.model.feature.user.user.NotificationSettings
import com.petbulance.domain.repository.feature.user.UserRepository
import javax.inject.Inject

class GetNotificationSettingsUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Result<NotificationSettings> {
        return repository.getNotificationSettings()
    }
}
