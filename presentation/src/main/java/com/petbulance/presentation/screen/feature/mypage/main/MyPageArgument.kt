package com.petbulance.presentation.screen.feature.mypage.main

import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class MyPageArgument(
    val intent: (MyPageIntent) -> Unit,
    val event: SharedFlow<MyPageEvent>
)

sealed class MyPageIntent

sealed class MyPageEvent {
    sealed class DataFetch : MyPageEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }
}
