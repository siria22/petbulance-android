package com.petbulance.presentation.screen.feature.mypage.sections.help.notice.list

import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class MyPageNoticeArgument(
    val intent: (MyPageNoticeIntent) -> Unit,
    val dataState: MyPageNoticeDataState,
    val event: SharedFlow<MyPageNoticeEvent>
)

sealed class MyPageNoticeDataState {
    data object Init : MyPageNoticeDataState()
    data object Loading : MyPageNoticeDataState()
}

sealed class MyPageNoticeIntent {
    data object LoadMore : MyPageNoticeIntent()
    data object Refresh : MyPageNoticeIntent()
}

sealed class MyPageNoticeEvent {
    sealed class DataFetch : MyPageNoticeEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }
}

