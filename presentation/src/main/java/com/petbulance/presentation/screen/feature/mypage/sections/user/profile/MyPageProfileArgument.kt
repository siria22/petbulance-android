package com.petbulance.presentation.screen.feature.mypage.sections.user.profile

import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class MyPageProfileArgument(
    val intent: (MyPageProfileIntent) -> Unit,
    val dataState: MyPageProfileDataState,
    val screenState: MyPageProfileScreenState,
    val event: SharedFlow<MyPageProfileEvent>
)

sealed class MyPageProfileDataState {
    data object Init : MyPageProfileDataState()
    data object OnProgress : MyPageProfileDataState()
}

sealed class MyPageProfileScreenState {
    data object Init : MyPageProfileScreenState()
}

sealed class MyPageProfileIntent {
    data class SomeIntentWithParamsMyPage(val param: String) : MyPageProfileIntent()
    data object SomeIntentWithoutParamsMyPage : MyPageProfileIntent()
}

sealed class MyPageProfileEvent {
    sealed class DataFetch : MyPageProfileEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }
}