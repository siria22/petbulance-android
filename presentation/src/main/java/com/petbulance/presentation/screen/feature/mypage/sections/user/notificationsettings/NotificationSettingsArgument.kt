package com.petbulance.presentation.screen.feature.mypage.sections.user.notificationsettings

import com.petbulance.domain.model.feature.user.user.NotificationSettings
import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class NotificationSettingsArgument(
    val intent: (NotificationSettingsIntent) -> Unit,
    val dataState: NotificationSettingsDataState,
    val event: SharedFlow<NotificationSettingsEvent>
)

sealed class NotificationSettingsDataState {
    data object Init : NotificationSettingsDataState()
    data object Loading : NotificationSettingsDataState()
}

data class NotificationSettingsData(
    val settings: NotificationSettings = NotificationSettings(
        isAllEnabled = false,
        isEventEnabled = false,
        isMarketingEnabled = false
    )
) {
    companion object {
        val empty = NotificationSettingsData()
    }
}

sealed interface NotificationSettingsIntent {
    data class ToggleAll(val enabled: Boolean) : NotificationSettingsIntent
    data class ToggleEvent(val enabled: Boolean) : NotificationSettingsIntent
    data class ToggleMarketing(val enabled: Boolean) : NotificationSettingsIntent
}

sealed class NotificationSettingsEvent {
    data class DataFetch(
        override val userMessage: String = "문제가 발생했습니다.",
        override val exceptionMessage: String?,
        override val displayType: ErrorDisplayType = ErrorDisplayType.Common
    ) : NotificationSettingsEvent(), ErrorEvent
}
