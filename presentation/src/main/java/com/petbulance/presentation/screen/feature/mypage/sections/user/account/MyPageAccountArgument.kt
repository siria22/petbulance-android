package com.petbulance.presentation.screen.feature.mypage.sections.user.account

import com.petbulance.domain.model.type.LoginProviderType
import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class MyPageAccountArgument(
    val intent: (MyPageAccountIntent) -> Unit,
    val dataState: MyPageAccountDataState,
    val event: SharedFlow<MyPageAccountEvent>
)

sealed class MyPageAccountDataState {
    data object Init : MyPageAccountDataState()
    data object OnProgress : MyPageAccountDataState()
}

sealed interface MyPageAccountIntent {
    data class ToggleAutoLogin(val isEnabled: Boolean) : MyPageAccountIntent
    data class ConnectSocial(val provider: LoginProviderType, val token: String) : MyPageAccountIntent
    data class DisconnectSocial(val provider: LoginProviderType) : MyPageAccountIntent
    data object Logout : MyPageAccountIntent
}

sealed class MyPageAccountEvent {
    sealed class DataFetch : MyPageAccountEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }

    data object LogoutSuccess : MyPageAccountEvent()

    sealed class SocialLink: MyPageAccountEvent() {
        data object IsLast: SocialLink()
        data class DisconnectFailed(
            override val userMessage: String = "계정 연동 해제에 실패했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : SocialLink(), ErrorEvent
    }
}