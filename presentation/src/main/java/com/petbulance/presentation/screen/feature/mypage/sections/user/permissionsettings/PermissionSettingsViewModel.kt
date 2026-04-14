package com.petbulance.presentation.screen.feature.mypage.sections.user.permissionsettings

import com.petbulance.domain.usecase.feature.user.user.GetAuthorityUseCase
import com.petbulance.domain.usecase.feature.user.user.ToggleAuthorityUseCase
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
class PermissionSettingsViewModel @Inject constructor(
    private val getAuthorityUseCase: GetAuthorityUseCase,
    private val toggleAuthorityUseCase: ToggleAuthorityUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<PermissionSettingsDataState>(PermissionSettingsDataState.Init)
    val dataState: StateFlow<PermissionSettingsDataState> = _dataState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<PermissionSettingsEvent>()
    val eventFlow: SharedFlow<PermissionSettingsEvent> = _eventFlow

    private val _settingsData = MutableStateFlow(PermissionSettingsData.empty)
    val settingsData: StateFlow<PermissionSettingsData> = _settingsData.asStateFlow()

    init {
        observeErrorEvent(_eventFlow)
        loadSettings()
    }

    fun onIntent(intent: PermissionSettingsIntent) {
        when (intent) {
            is PermissionSettingsIntent.ToggleLocation -> togglePermission("locationService")
            is PermissionSettingsIntent.ToggleMarketing -> togglePermission("marketing")
            is PermissionSettingsIntent.ToggleCamera -> togglePermission("camera")
            is PermissionSettingsIntent.UpdateFromOs -> {
                _settingsData.update {
                    it.copy(
                        settings = it.settings.copy(
                            locationService = intent.location,
                            camera = intent.camera
                        )
                    )
                }
            }
        }
    }

    private fun loadSettings() {
        launch {
            _dataState.value = PermissionSettingsDataState.Loading
            getAuthorityUseCase()
                .onSuccess { settings ->
                    _settingsData.update { it.copy(settings = settings) }
                }
                .onFailure { e ->
                    _eventFlow.emit(
                        PermissionSettingsEvent.DataFetch(
                            userMessage = "권한 설정을 불러오는데 실패했습니다.",
                            exceptionMessage = e.message
                        )
                    )
                }
            _dataState.value = PermissionSettingsDataState.Init
        }
    }

    private fun togglePermission(type: String) {
        val current = _settingsData.value.settings
        val isOsPermission = type == "locationService" || type == "camera"
        val currentValue = when (type) {
            "locationService" -> current.locationService
            "camera" -> current.camera
            "marketing" -> current.marketing
            else -> return
        }

        // OS 권한이 필요한 항목이면서 현재 OFF → ON 전환 시, 앱 설정으로 이동 안내
        if (isOsPermission && !currentValue) {
            launch { _eventFlow.emit(PermissionSettingsEvent.OpenAppSettings) }
            return
        }

        // 낙관적 업데이트
        _settingsData.update {
            it.copy(
                settings = when (type) {
                    "locationService" -> it.settings.copy(locationService = !currentValue)
                    "camera" -> it.settings.copy(camera = !currentValue)
                    "marketing" -> it.settings.copy(marketing = !currentValue)
                    else -> it.settings
                }
            )
        }

        launch {
            toggleAuthorityUseCase(type)
                .onFailure { e ->
                    // 롤백
                    _settingsData.update {
                        it.copy(
                            settings = when (type) {
                                "locationService" -> it.settings.copy(locationService = currentValue)
                                "camera" -> it.settings.copy(camera = currentValue)
                                "marketing" -> it.settings.copy(marketing = currentValue)
                                else -> it.settings
                            }
                        )
                    }
                    _eventFlow.emit(
                        PermissionSettingsEvent.DataFetch(
                            userMessage = "권한 변경에 실패했습니다.",
                            exceptionMessage = e.message
                        )
                    )
                }
        }
    }
}
