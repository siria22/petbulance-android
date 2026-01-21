package com.petbulance.presentation.screen.feature.search.main

import android.location.Location
import kotlinx.coroutines.flow.SharedFlow

data class UserLocationArgument(
    val intent: (UserLocationIntent) -> Unit,
    val locationState: UserLocationState,
    val event: SharedFlow<SearchEvent>
)

sealed class UserLocationState {
    data object Init : UserLocationState()
    data object PermissionRequired : UserLocationState() // 권한 필요
    data object Finding : UserLocationState() // 위치 찾는 중
    data class Success(val location: Location) : UserLocationState() // 완료
    data class Failed(val message: String) : UserLocationState() // 실패
}

sealed class UserLocationIntent {
    data class PermissionResult(val isGranted: Boolean) : UserLocationIntent()
    data object RequestLocation : UserLocationIntent()
}
