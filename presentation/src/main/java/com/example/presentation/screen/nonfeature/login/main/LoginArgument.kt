package com.example.presentation.screen.nonfeature.login.main

import com.example.domain.model.type.LoginProviderType
import com.example.presentation.utils.error.ErrorDisplayType
import com.example.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class LoginArgument(
    val intent: (LoginIntent) -> Unit,
    val dataState: LoginDataState,
    val screenState: LoginScreenState,
    val event: SharedFlow<LoginEvent>
)

sealed class LoginDataState {
    data object Init : LoginDataState()
    data object Loading : LoginDataState()
}

sealed class LoginScreenState {
    data object Init : LoginScreenState()
}

sealed class LoginIntent {
    data class OnSocialLogin(
        val provider: LoginProviderType,
        val token: String
    ) : LoginIntent()
}

sealed class LoginEvent {
    data object NavigateToHome : LoginEvent()
    data object NavigateToTerms : LoginEvent()

    sealed class DataFetch : LoginEvent() {
        data class Error(
            override val userMessage: String = "로그인에 실패했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }
}