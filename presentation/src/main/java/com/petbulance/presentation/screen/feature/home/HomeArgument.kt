package com.petbulance.presentation.screen.feature.home

import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class HomeArgument(
    val intent: (HomeIntent) -> Unit,
    val dataState: HomeDataState,
    val screenState: HomeScreenState,
    val event: SharedFlow<HomeEvent>
)

sealed class HomeDataState {
    data object Init : HomeDataState()
    data object OnProgress : HomeDataState()
}

sealed class HomeScreenState {
    data object Init: HomeScreenState()
}

sealed class HomeIntent {
    data class SomeIntentWithParams(val param: String) : HomeIntent()
    data object SomeIntentWithoutParams : HomeIntent()
}

sealed class HomeEvent {
    sealed class DataFetch : HomeEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }
}