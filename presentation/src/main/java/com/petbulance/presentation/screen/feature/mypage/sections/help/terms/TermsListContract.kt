package com.petbulance.presentation.screen.feature.mypage.sections.help.terms

import com.petbulance.domain.model.feature.user.terms.Term
import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class TermsListArgument(
    val intent: (TermsListIntent) -> Unit,
    val dataState: TermsListDataState,
    val screenState: TermsListScreenState,
    val event: SharedFlow<TermsListEvent>
)

data class TermsListData(
    val termsList: List<Term>
) {
    companion object {
        fun stub() = TermsListData(
            termsList = listOf(
                Term.stub(),
                Term.stub().copy(id = 2, title = "펫뷸런스 개인정보 처리방침", required = true),
                Term.stub().copy(id = 3, title = "운영정책", required = false),
                Term.stub().copy(id = 4, title = "위치기반서비스 이용약관", required = false),
                Term.stub().copy(id = 5, title = "마케팅 정보 수신 동의 약관", required = false)
            )
        )
    }
}

sealed class TermsListDataState {
    data object Init : TermsListDataState()
    data object Loading : TermsListDataState()
    data object Loaded : TermsListDataState()
}

sealed class TermsListScreenState {
    data object Init : TermsListScreenState()
    data class ShowRequiredTermsDialog(val term: Term) : TermsListScreenState()
}

sealed class TermsListIntent {
    data object OnRefresh : TermsListIntent()
    data class OnToggleChanged(val term: Term, val isAgreed: Boolean) : TermsListIntent()
    data object DismissRequiredTermsDialog : TermsListIntent()
}

sealed class TermsListEvent {
    sealed class DataFetch : TermsListEvent() {
        data class Error(
            override val userMessage: String = "약관 목록을 불러오는데 실패했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }

    sealed class TermsWithdraw : TermsListEvent() {
        data class Error(
            override val userMessage: String = "약관 철회에 실패했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : TermsWithdraw(), ErrorEvent
    }
}
