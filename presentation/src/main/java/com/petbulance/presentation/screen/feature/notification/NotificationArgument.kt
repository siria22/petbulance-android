package com.petbulance.presentation.screen.feature.notification

import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class NotificationArgument(
    val intent: (NotificationIntent) -> Unit,
    val dataState: NotificationDataState,
    val event: SharedFlow<NotificationEvent>
)

sealed class NotificationDataState {
    data object Init : NotificationDataState()
    data object Loading : NotificationDataState()
}

sealed interface NotificationIntent {
    data object LoadMoreNotices : NotificationIntent
    data object LoadMoreNotifications : NotificationIntent
    data object ReadAllNotifications : NotificationIntent
    data object DeleteAllNotifications : NotificationIntent
    data class SwitchTab(val tab: NotificationTab) : NotificationIntent
}

enum class NotificationTab(val korean: String) {
    NOTICE("공지사항"),
    ACTIVITY("내 활동")
}

sealed class NotificationEvent {
    data class ShowSnackbar(val message: String) : NotificationEvent()

    sealed class DataFetch : NotificationEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }
}
