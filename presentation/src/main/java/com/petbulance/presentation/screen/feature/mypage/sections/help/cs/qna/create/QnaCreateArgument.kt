package com.petbulance.presentation.screen.feature.mypage.sections.help.cs.qna.create

import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class QnaCreateArgument(
    val intent: (QnaCreateIntent) -> Unit,
    val dataState: QnaCreateDataState,
    val screenState: QnaCreateScreenState,
    val event: SharedFlow<QnaCreateEvent>
)

sealed class QnaCreateDataState {
    data object Init : QnaCreateDataState()
    data object OnProgress : QnaCreateDataState()
}

sealed class QnaCreateScreenState {
    data object Init : QnaCreateScreenState()
}

enum class QnaCreateMode {
    CREATE,
    EDIT
}

sealed class QnaCreateIntent {
    data class OnTitleChanged(val title: String) : QnaCreateIntent()
    data class OnContentChanged(val content: String) : QnaCreateIntent()
    data object OnSubmitClicked : QnaCreateIntent()
}

sealed class QnaCreateEvent {
    data class SubmitSuccess(val qnaId: Long) : QnaCreateEvent()
    
    sealed class Submit : QnaCreateEvent() {
        data class Error(
            override val userMessage: String = "문의 등록에 실패했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : Submit(), ErrorEvent
    }
    
    sealed class DataFetch : QnaCreateEvent() {
        data class Error(
            override val userMessage: String = "문의 내역을 불러오는데 실패했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }
}