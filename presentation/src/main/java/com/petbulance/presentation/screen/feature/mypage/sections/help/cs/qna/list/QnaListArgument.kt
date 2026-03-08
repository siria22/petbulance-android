package com.petbulance.presentation.screen.feature.mypage.sections.help.cs.qna.list

import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class QnaListArgument(
    val intent: (QnaListIntent) -> Unit,
    val dataState: QnaListDataState,
    val screenState: QnaListScreenState,
    val event: SharedFlow<QnaListEvent>
)

sealed class QnaListDataState {
    data object Init : QnaListDataState()
    data object Loading : QnaListDataState()
    data class Loaded(val hasNext: Boolean) : QnaListDataState()
}

sealed class QnaListScreenState {
    data object Init : QnaListScreenState()
}

sealed class QnaListIntent {
    data object OnRefresh : QnaListIntent()
    data object OnLoadMore : QnaListIntent()
}

sealed class QnaListEvent {
    sealed class DataFetch : QnaListEvent() {
        data class Error(
            override val userMessage: String = "문의 목록을 불러오는데 실패했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }
}
