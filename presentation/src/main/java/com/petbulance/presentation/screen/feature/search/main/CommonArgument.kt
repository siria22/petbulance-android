package com.petbulance.presentation.screen.feature.search.main

import android.location.Location
import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent

data class CommonSearchArgument(
    val screenState: SearchScreenState,
    val intent: (SearchIntent) -> Unit
)

sealed class SearchScreenState {

    sealed class Hospitals : SearchScreenState() {
        data object MapView : SearchScreenState()
        data object ListView : SearchScreenState()
    }

    sealed class OnSearch : SearchScreenState() {
        data object SearchView : SearchScreenState()
        data object ResultView : SearchScreenState()
    }
}

sealed class SearchIntent {
    data class ChangeScreenState(val state: SearchScreenState) : SearchIntent()
}

sealed class SearchEvent {
    sealed class DataFetch : SearchEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }

    sealed class UserLocation : SearchEvent() {
        sealed class CheckPermission() : UserLocation() {
            data class Error(
                override val userMessage: String = "위치 권한을 허용해주세요.",
                override val exceptionMessage: String?,
                override val displayType: ErrorDisplayType = ErrorDisplayType.Custom
            ) : CheckPermission(), ErrorEvent
        }

        data class MoveCamera(val location: Location) : UserLocation()
    }
}