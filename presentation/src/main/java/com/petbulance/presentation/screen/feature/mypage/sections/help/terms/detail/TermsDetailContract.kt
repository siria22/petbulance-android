package com.petbulance.presentation.screen.feature.mypage.sections.help.terms.detail

import com.petbulance.domain.model.feature.user.terms.Term
import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class TermsDetailArgument(
    val intent: (TermsDetailIntent) -> Unit,
    val dataState: TermsDetailDataState,
    val screenState: TermsDetailScreenState,
    val event: SharedFlow<TermsDetailEvent>
)

data class TermsDetailData(
    val term: Term?,
    val isAgreed: Boolean
) {
    companion object {
        fun stub() = TermsDetailData(
            term = Term.stub(),
            isAgreed = true
        )
    }
}

sealed class TermsDetailDataState {
    data object Init : TermsDetailDataState()
    data object Loading : TermsDetailDataState()
    data object Loaded : TermsDetailDataState()
}

sealed class TermsDetailScreenState {
    data object Init : TermsDetailScreenState()
    data class ShowRequiredTermsDialog(val term: Term) : TermsDetailScreenState()
    data object ShowContentLoadFailedDialog : TermsDetailScreenState()
}

sealed class TermsDetailIntent {
    data object OnRefresh : TermsDetailIntent()
    data class OnToggleChanged(val isAgreed: Boolean) : TermsDetailIntent()
    data object DismissRequiredTermsDialog : TermsDetailIntent()
    data object DismissContentLoadFailedDialog : TermsDetailIntent()
}

sealed class TermsDetailEvent {
    sealed class DataFetch : TermsDetailEvent() {
        data class Error(
            override val userMessage: String = "약관 상세 정보를 불러오는데 실패했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }

    sealed class TermsWithdraw : TermsDetailEvent() {
        data class Error(
            override val userMessage: String = "약관 철회에 실패했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : TermsWithdraw(), ErrorEvent
    }
}
