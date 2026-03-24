package com.petbulance.presentation.screen.feature.mypage.sections.activity.posts

import com.petbulance.domain.model.feature.community.post.MyPostSummary
import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class MyPagePostsArgument(
    val intent: (MyPagePostsIntent) -> Unit,
    val dataState: MyPagePostsDataState,
    val screenState: MyPagePostsScreenState,
    val event: SharedFlow<MyPagePostsEvent>
)

sealed class MyPagePostsDataState {
    data object Init : MyPagePostsDataState()
    data object Loading : MyPagePostsDataState()
    data class Loaded(
        val posts: List<MyPostSummary>,
        val hasNext: Boolean = false
    ) : MyPagePostsDataState()
}

sealed class MyPagePostsScreenState {
    data object Init : MyPagePostsScreenState()
    data class Normal(
        val isSelectionMode: Boolean = false,
        val selectedIds: Set<Long> = emptySet()
    ) : MyPagePostsScreenState()
}

sealed class MyPagePostsIntent {
    data object LoadData : MyPagePostsIntent()
    data object LoadMore : MyPagePostsIntent()
    data object Refresh : MyPagePostsIntent()

    data class ToggleSelectionMode(val enabled: Boolean) : MyPagePostsIntent()
    data class TogglePostSelection(val postId: Long) : MyPagePostsIntent()
    data object SelectAll : MyPagePostsIntent()
    data object DeleteSelected : MyPagePostsIntent()
}

sealed class MyPagePostsEvent {
    sealed class DataFetch : MyPagePostsEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }

    sealed class Post : MyPagePostsEvent() {
        data object DeleteSuccess : Post()

        data class DeleteFailed(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }
}
