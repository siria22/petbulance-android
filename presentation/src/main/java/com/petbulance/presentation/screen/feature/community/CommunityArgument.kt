package com.petbulance.presentation.screen.feature.community

import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class CommunityArgument(
    val intent: (CommunityIntent) -> Unit,
    val dataState: CommunityDataState,
    val screenState: CommunityScreenState,
    val event: SharedFlow<CommunityEvent>
)

sealed class CommunityDataState {
    data object Init : CommunityDataState()
    data object OnProgress : CommunityDataState()
}

sealed class CommunityScreenState {
    data object Init : CommunityScreenState()
}

sealed class CommunityIntent {
    data class SomeIntentWithParams(val param: String) : CommunityIntent()
    data object SomeIntentWithoutParams : CommunityIntent()
}

sealed class CommunityEvent {
    sealed class DataFetch : CommunityEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }
}