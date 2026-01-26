package com.petbulance.presentation.screen.nonfeature.login.terms

import com.petbulance.domain.model.feature.user.terms.Term
import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class TermsArgument(
    val intent: (TermsIntent) -> Unit,
    val dataState: TermsDataState,
    val screenState: TermsScreenState,
    val event: SharedFlow<TermsEvent>
)

sealed class TermsDataState {
    data object Init : TermsDataState()
    data object Loading : TermsDataState()
}

sealed class TermsScreenState {
    data object Init : TermsScreenState()
}

sealed class TermsIntent {
    data object OnAgreeClick : TermsIntent()
    data class OnToggleTerm(val term: Term) : TermsIntent()
    data object OnToggleAll : TermsIntent()

    data class OnDetailClick(val term: Term) : TermsIntent()
    data object OnCloseDetail : TermsIntent()
}

sealed class TermsEvent {
    data object NavigateToNext : TermsEvent()

    sealed class DataFetch : TermsEvent() {
        data class Error(
            override val userMessage: String = "약관 처리에 실패했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }
}