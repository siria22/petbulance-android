package com.petbulance.presentation.screen.feature.mypage.sections.user.withdrawal

import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class WithdrawalArgument(
    val intent: (WithdrawalIntent) -> Unit,
    val dataState: WithdrawalDataState,
    val event: SharedFlow<WithdrawalEvent>
)

sealed class WithdrawalDataState {
    data object Init : WithdrawalDataState()
    data object OnProgress : WithdrawalDataState()
}

sealed interface WithdrawalIntent {
    data object ConfirmWithdrawal : WithdrawalIntent
}

sealed class WithdrawalEvent {
    data object WithdrawalSuccess : WithdrawalEvent()
    data class WithdrawalFailed(
        override val userMessage: String = "탈퇴 처리에 실패했습니다.",
        override val exceptionMessage: String?,
        override val displayType: ErrorDisplayType = ErrorDisplayType.Common
    ) : WithdrawalEvent(), ErrorEvent
}
