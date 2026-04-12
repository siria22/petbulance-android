package com.petbulance.presentation.screen.feature.community.write

import android.net.Uri
import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class WritePostArgument(
    val intent: (WritePostIntent) -> Unit,
    val dataState: WritePostDataState,
    val screenState: WritePostScreenState,
    val event: SharedFlow<WritePostEvent>
)

sealed class WritePostDataState {
    data object Idle : WritePostDataState()
    data object Loading : WritePostDataState()
    data object Submitting : WritePostDataState()
}

sealed class WritePostScreenState {
    data object Idle : WritePostScreenState()
}

sealed class WritePostIntent {
    data class SelectAnimalType(val type: String) : WritePostIntent()
    data class SelectTopic(val topic: String) : WritePostIntent()
    data class UpdateTitle(val title: String) : WritePostIntent()
    data class UpdateContent(val content: String) : WritePostIntent()
    data class AddImage(val uri: Uri) : WritePostIntent()
    data class RemoveImage(val index: Int) : WritePostIntent()
    data object NavigateUp : WritePostIntent()
}

sealed class WritePostEvent {
    data class NavigateToPostDetail(val postId: Long) : WritePostEvent()
    data object NavigateUp : WritePostEvent()
    data class SubmitError(
        override val userMessage: String,
        override val exceptionMessage: String?,
        override val displayType: ErrorDisplayType = ErrorDisplayType.Common
    ) : WritePostEvent(), ErrorEvent
}
