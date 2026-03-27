package com.petbulance.presentation.screen.feature.mypage.sections.user.profile

import android.net.Uri
import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class MyPageProfileArgument(
    val intent: (MyPageProfileIntent) -> Unit,
    val dataState: MyPageProfileDataState,
    val event: SharedFlow<MyPageProfileEvent>
)

sealed class MyPageProfileDataState {
    data object Init : MyPageProfileDataState()
    data object OnProgress : MyPageProfileDataState()
}

sealed class MyPageProfileIntent {
    data object LoadUserInfo : MyPageProfileIntent()
    data class SelectImage(val uri: Uri) : MyPageProfileIntent()
    data class SaveProfile(
        val newNickname: String,
        val imageUriString: String?
    ) : MyPageProfileIntent()
}

sealed class MyPageProfileEvent {
    data object SaveSuccess : MyPageProfileEvent()
    sealed class DataFetch : MyPageProfileEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }
}
