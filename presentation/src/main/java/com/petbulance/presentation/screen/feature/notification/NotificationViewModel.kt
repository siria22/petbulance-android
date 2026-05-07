package com.petbulance.presentation.screen.feature.notification

import com.petbulance.domain.usecase.feature.notification.DeleteAllNotificationsUseCase
import com.petbulance.domain.usecase.feature.notification.GetNotificationListUseCase
import com.petbulance.domain.usecase.feature.notification.ReadAllNotificationsUseCase
import com.petbulance.domain.usecase.feature.support.notice.GetNoticeListUseCase
import com.petbulance.presentation.utils.BaseViewModel
import com.petbulance.presentation.utils.error.ErrorDisplayType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getNoticeListUseCase: GetNoticeListUseCase,
    private val getNotificationListUseCase: GetNotificationListUseCase,
    private val readAllNotificationsUseCase: ReadAllNotificationsUseCase,
    private val deleteAllNotificationsUseCase: DeleteAllNotificationsUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<NotificationDataState>(NotificationDataState.Init)
    val dataState: StateFlow<NotificationDataState> = _dataState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<NotificationEvent>()
    val eventFlow: SharedFlow<NotificationEvent> = _eventFlow

    private val _notificationData = MutableStateFlow(NotificationData.empty)
    val notificationData: StateFlow<NotificationData> = _notificationData.asStateFlow()

    // Notice tab pagination
    private var lastNoticeId: Long? = null
    private var hasNextNotice = true
    private var isNoticeLoading = false

    // Activity tab pagination
    private var lastNotificationId: Long? = null
    private var hasNextNotification = true
    private var isNotificationLoading = false
    private var activityTabLoaded = false

    init {
        observeErrorEvent(_eventFlow)
        loadNotices(isRefresh = true)
    }

    fun onIntent(intent: NotificationIntent) {
        when (intent) {
            is NotificationIntent.LoadMoreNotices -> {
                if (!isNoticeLoading && hasNextNotice) loadNotices(isRefresh = false)
            }
            is NotificationIntent.LoadMoreNotifications -> {
                if (!isNotificationLoading && hasNextNotification) loadNotifications(isRefresh = false)
            }
            is NotificationIntent.ReadAllNotifications -> readAllNotifications()
            is NotificationIntent.DeleteAllNotifications -> deleteAllNotifications()
            is NotificationIntent.SwitchTab -> switchTab(intent.tab)
        }
    }

    private fun switchTab(tab: NotificationTab) {
        _notificationData.update { it.copy(selectedTab = tab) }
        if (tab == NotificationTab.ACTIVITY && !activityTabLoaded) {
            activityTabLoaded = true
            loadNotifications(isRefresh = true)
        }
    }

    private fun loadNotices(isRefresh: Boolean) {
        launch {
            if (isRefresh) {
                _dataState.value = NotificationDataState.Loading
                lastNoticeId = null
                hasNextNotice = true
            } else {
                _notificationData.update { it.copy(isNoticeLoadingNextPage = true) }
            }
            isNoticeLoading = true

            getNoticeListUseCase(lastNoticeId = lastNoticeId, pageSize = PAGE_SIZE)
                .onSuccess { pagingResult ->
                    lastNoticeId = pagingResult.content.lastOrNull()?.noticeId
                    hasNextNotice = pagingResult.hasNext
                    _notificationData.update {
                        it.copy(
                            notices = if (isRefresh) pagingResult.content else it.notices + pagingResult.content,
                            isNoticeLoadingNextPage = false
                        )
                    }
                }
                .onFailure { emitError("공지사항을 불러오는데 실패했습니다.", it) }

            if (isRefresh) _dataState.value = NotificationDataState.Init
            isNoticeLoading = false
        }
    }

    private fun loadNotifications(isRefresh: Boolean) {
        launch {
            if (isRefresh) {
                _dataState.value = NotificationDataState.Loading
                lastNotificationId = null
                hasNextNotification = true
            } else {
                _notificationData.update { it.copy(isNotificationLoadingNextPage = true) }
            }
            isNotificationLoading = true

            getNotificationListUseCase(lastNotificationId = lastNotificationId, pageSize = PAGE_SIZE)
                .onSuccess { pagingResult ->
                    lastNotificationId = pagingResult.lastNotificationId
                    hasNextNotification = pagingResult.hasNext
                    _notificationData.update {
                        it.copy(
                            notifications = if (isRefresh) pagingResult.content else it.notifications + pagingResult.content,
                            isNotificationLoadingNextPage = false
                        )
                    }
                }
                .onFailure { emitError("알림을 불러오는데 실패했습니다.", it) }

            if (isRefresh) _dataState.value = NotificationDataState.Init
            isNotificationLoading = false
        }
    }

    private fun readAllNotifications() {
        launch {
            readAllNotificationsUseCase()
                .onSuccess {
                    _notificationData.update { data ->
                        data.copy(
                            notifications = data.notifications.map { it.copy(isRead = true) }
                        )
                    }
                }
                .onFailure { emitError("전체 읽음 처리에 실패했습니다.", it) }
        }
    }

    private fun deleteAllNotifications() {
        launch {
            deleteAllNotificationsUseCase()
                .onSuccess {
                    _notificationData.update { it.copy(notifications = emptyList()) }
                    lastNotificationId = null
                    hasNextNotification = false
                    _eventFlow.emit(NotificationEvent.ShowSnackbar("알림함을 비웠어요."))
                }
                .onFailure { emitError("알림 삭제에 실패했습니다.", it) }
        }
    }

    private fun emitError(message: String, exception: Throwable) {
        _eventFlow.tryEmit(
            NotificationEvent.DataFetch.Error(
                displayType = ErrorDisplayType.Common,
                userMessage = message,
                exceptionMessage = exception.message
            )
        )
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
