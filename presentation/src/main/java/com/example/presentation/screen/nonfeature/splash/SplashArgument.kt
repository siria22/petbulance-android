package com.example.presentation.screen.nonfeature.splash

import com.example.presentation.utils.error.ErrorDisplayType
import com.example.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class SplashArgument(
    val intent: (SplashIntent) -> Unit,
    val event: SharedFlow<SplashEvent>
)

sealed class SplashDataState {
    data object Init : SplashDataState()
    data object OnProgress : SplashDataState()
}

sealed class SplashScreenState {
    data object Init : SplashScreenState()
}

sealed class SplashIntent {
    data class SomeIntentWithParams(val param: String) : SplashIntent()
    data object SomeIntentWithoutParams : SplashIntent()
}

sealed class SplashEvent {
    data object NavigateToLogin : SplashEvent()
    data object NavigateToHome : SplashEvent()
    data object NavigateToHomeWithTermsCheck : SplashEvent()

    sealed class DataFetch : SplashEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }
}