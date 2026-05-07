package com.petbulance.presentation.screen.feature.mypage.sections.user.notificationsettings

import com.petbulance.domain.model.feature.user.user.NotificationSettings
import com.petbulance.domain.usecase.feature.user.user.GetNotificationSettingsUseCase
import com.petbulance.domain.usecase.feature.user.user.UpdateNotificationSettingsUseCase
import com.petbulance.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsViewModel @Inject constructor(
    private val getNotificationSettingsUseCase: GetNotificationSettingsUseCase,
    private val updateNotificationSettingsUseCase: UpdateNotificationSettingsUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<NotificationSettingsDataState>(NotificationSettingsDataState.Init)
    val dataState: StateFlow<NotificationSettingsDataState> = _dataState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<NotificationSettingsEvent>()
    val eventFlow: SharedFlow<NotificationSettingsEvent> = _eventFlow

    private val _settingsData = MutableStateFlow(NotificationSettingsData.empty)
    val settingsData: StateFlow<NotificationSettingsData> = _settingsData.asStateFlow()

    init {
        observeErrorEvent(_eventFlow)
        loadSettings()
    }

    fun onIntent(intent: NotificationSettingsIntent) {
        when (intent) {
            is NotificationSettingsIntent.ToggleAll -> updateSettings(
                _settingsData.value.settings.copy(isAllEnabled = intent.enabled)
            )
            is NotificationSettingsIntent.ToggleEvent -> updateSettings(
                _settingsData.value.settings.copy(isEventEnabled = intent.enabled)
            )
            is NotificationSettingsIntent.ToggleMarketing -> updateSettings(
                _settingsData.value.settings.copy(isMarketingEnabled = intent.enabled)
            )
        }
    }

    private fun loadSettings() {
        launch {
            _dataState.value = NotificationSettingsDataState.Loading
            getNotificationSettingsUseCase()
                .onSuccess { settings ->
                    _settingsData.update { it.copy(settings = settings) }
                }
                .onFailure { e ->
                    _eventFlow.emit(
                        NotificationSettingsEvent.DataFetch(
                            userMessage = "알림 설정을 불러오는데 실패했습니다.",
                            exceptionMessage = e.message
                        )
                    )
                }
            _dataState.value = NotificationSettingsDataState.Init
        }
    }

    private fun updateSettings(newSettings: NotificationSettings) {
        // 낙관적 업데이트
        val previousSettings = _settingsData.value.settings
        _settingsData.update { it.copy(settings = newSettings) }

        launch {
            updateNotificationSettingsUseCase(newSettings)
                .onSuccess { serverSettings ->
                    _settingsData.update { it.copy(settings = serverSettings) }
                }
                .onFailure { e ->
                    // 롤백
                    _settingsData.update { it.copy(settings = previousSettings) }
                    _eventFlow.emit(
                        NotificationSettingsEvent.DataFetch(
                            userMessage = "알림 설정 변경에 실패했습니다.",
                            exceptionMessage = e.message
                        )
                    )
                }
        }
    }
}
