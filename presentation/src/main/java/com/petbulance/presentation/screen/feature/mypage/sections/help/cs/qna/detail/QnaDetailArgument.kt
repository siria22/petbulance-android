package com.petbulance.presentation.screen.feature.mypage.sections.help.cs.qna.detail

import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class QnaDetailArgument(
    val intent: (QnaDetailIntent) -> Unit,
    val dataState: QnaDetailDataState,
    val event: SharedFlow<QnaDetailEvent>
)

sealed class QnaDetailDataState {
    data object Init : QnaDetailDataState()
    data object OnProgress : QnaDetailDataState()
}

sealed class QnaDetailIntent {
    data object OnDeleteConfirmed : QnaDetailIntent()
}

sealed class QnaDetailEvent {
    sealed class DataFetch : QnaDetailEvent() {
        data class Error(
            override val userMessage: String = "문의 내역을 불러오는데 실패했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }

    sealed class Delete : QnaDetailEvent() {
        data object Success : Delete()
        data class Error(
            override val userMessage: String = "문의 삭제에 실패했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : Delete(), ErrorEvent
    }
}