package com.petbulance.presentation.screen.feature.mypage.sections.help.cs

import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class CSArgument(
    val intent: (CSIntent) -> Unit,
    val dataState: CSDataState,
    val screenState: CSScreenState,
    val event: SharedFlow<CSEvent>
)

sealed class CSDataState {
    data object Init : CSDataState()
    data object OnProgress : CSDataState()
}

sealed class CSScreenState {
    data object Init : CSScreenState()
}

sealed class CSIntent

sealed class CSEvent {
    sealed class DataFetch : CSEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }
}