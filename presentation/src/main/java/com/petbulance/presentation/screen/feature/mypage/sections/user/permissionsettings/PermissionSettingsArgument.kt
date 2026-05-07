package com.petbulance.presentation.screen.feature.mypage.sections.user.permissionsettings

import com.petbulance.domain.model.feature.user.user.AuthoritySettings
import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class PermissionSettingsArgument(
    val intent: (PermissionSettingsIntent) -> Unit,
    val dataState: PermissionSettingsDataState,
    val event: SharedFlow<PermissionSettingsEvent>
)

sealed class PermissionSettingsDataState {
    data object Init : PermissionSettingsDataState()
    data object Loading : PermissionSettingsDataState()
}

data class PermissionSettingsData(
    val settings: AuthoritySettings = AuthoritySettings(
        locationService = false,
        marketing = false,
        camera = false
    )
) {
    companion object {
        val empty = PermissionSettingsData()
    }
}

sealed interface PermissionSettingsIntent {
    data object ToggleLocation : PermissionSettingsIntent
    data object ToggleMarketing : PermissionSettingsIntent
    data object ToggleCamera : PermissionSettingsIntent
    data class UpdateFromOs(val location: Boolean, val camera: Boolean) : PermissionSettingsIntent
}

sealed class PermissionSettingsEvent {
    data object OpenAppSettings : PermissionSettingsEvent()

    data class DataFetch(
        override val userMessage: String = "문제가 발생했습니다.",
        override val exceptionMessage: String?,
        override val displayType: ErrorDisplayType = ErrorDisplayType.Common
    ) : PermissionSettingsEvent(), ErrorEvent
}
