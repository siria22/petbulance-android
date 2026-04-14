package com.petbulance.domain.usecase.feature.user.user

import com.petbulance.domain.model.feature.user.user.NotificationSettings
import com.petbulance.domain.repository.feature.user.UserRepository
import javax.inject.Inject

class UpdateNotificationSettingsUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(settings: NotificationSettings): Result<NotificationSettings> {
        return repository.updateNotificationSettings(settings)
    }
}
