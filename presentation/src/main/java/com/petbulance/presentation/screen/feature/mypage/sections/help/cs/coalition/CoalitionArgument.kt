package com.petbulance.presentation.screen.feature.mypage.sections.help.cs.coalition

import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class CoalitionArgument(
    val intent: (CoalitionIntent) -> Unit,
    val dataState: CoalitionDataState,
    val event: SharedFlow<CoalitionEvent>
)

sealed class CoalitionDataState {
    data object Init : CoalitionDataState()
    data object OnProgress : CoalitionDataState()
}

sealed class CoalitionIntent {
    data class OnInquiryTypeChanged(val type: String) : CoalitionIntent()
    data class OnCompanyNameChanged(val name: String) : CoalitionIntent()
    data class OnManagerNameChanged(val name: String) : CoalitionIntent()
    data class OnManagerPositionChanged(val position: String) : CoalitionIntent()
    data class OnPhoneChanged(val phone: String) : CoalitionIntent()
    data class OnEmailChanged(val email: String) : CoalitionIntent()
    data class OnInterestTypeToggled(val type: String) : CoalitionIntent()
    data class OnContentChanged(val content: String) : CoalitionIntent()
    data class OnPrivacyConsentChanged(val consent: Boolean) : CoalitionIntent()
    data object OnSubmitClicked : CoalitionIntent()
}

sealed class CoalitionEvent {
    data class SubmitSuccess(val message: String) : CoalitionEvent()
    
    sealed class Submit : CoalitionEvent() {
        data class Error(
            override val userMessage: String = "문의 제출에 실패했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : Submit(), ErrorEvent
    }
}