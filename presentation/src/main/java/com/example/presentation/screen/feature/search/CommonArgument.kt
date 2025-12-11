package com.example.presentation.screen.feature.search

import com.example.presentation.utils.error.ErrorDisplayType
import com.example.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class CommonSearchArgument(
    val screenState: SearchScreenState,
    val event: SharedFlow<SearchEvent>
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

sealed class SearchEvent {
    sealed class DataFetch : SearchEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }

    sealed class Location : SearchEvent() {
        sealed class CheckPermission() : Location() {
            data class Error (
                override val userMessage: String = "위치 권한을 허용해주세요.",
                override val exceptionMessage: String?,
                override val displayType: ErrorDisplayType = ErrorDisplayType.Custom
            ) : CheckPermission(), ErrorEvent
        }
    }
}