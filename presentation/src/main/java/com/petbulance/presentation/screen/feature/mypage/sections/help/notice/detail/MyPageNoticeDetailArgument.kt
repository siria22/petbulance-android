package com.petbulance.presentation.screen.feature.mypage.sections.help.notice.detail

import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class MyPageNoticeDetailArgument(
    val intent: (MyPageNoticeDetailIntent) -> Unit,
    val dataState: MyPageNoticeDetailDataState,
    val screenState: MyPageNoticeDetailScreenState,
    val event: SharedFlow<MyPageNoticeDetailEvent>
)

sealed class MyPageNoticeDetailDataState {
    data object Init : MyPageNoticeDetailDataState()
    data object Loading : MyPageNoticeDetailDataState()
    data object NotFound : MyPageNoticeDetailDataState()
}

sealed class MyPageNoticeDetailScreenState {
    data object Init : MyPageNoticeDetailScreenState()
}

sealed class MyPageNoticeDetailIntent {
    data class LoadDetail(val noticeId: Long) : MyPageNoticeDetailIntent()
}

sealed class MyPageNoticeDetailEvent {
    sealed class DataFetch : MyPageNoticeDetailEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }
}
