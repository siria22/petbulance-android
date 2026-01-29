package com.petbulance.presentation.screen.feature.mypage.profile

import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class ProfileArgument(
    val intent: (ProfileIntent) -> Unit,
    val dataState: ProfileDataState,
    val screenState: ProfileScreenState,
    val event: SharedFlow<ProfileEvent>
)

sealed class ProfileDataState {
    data object Init : ProfileDataState()
    data object OnProgress : ProfileDataState()
}

sealed class ProfileScreenState {
    data object Init : ProfileScreenState()
}

sealed class ProfileIntent {
    data class SomeIntentWithParams(val param: String) : ProfileIntent()
    data object SomeIntentWithoutParams : ProfileIntent()
}

sealed class ProfileEvent {
    sealed class DataFetch : ProfileEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }
}