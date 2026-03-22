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
    data object Loading : CommunityDataState()
    data object LoadingMore : CommunityDataState()
}

sealed class CommunityScreenState {
    data object Home : CommunityScreenState()

    data object Search : CommunityScreenState()

    sealed class Result: CommunityScreenState() {
        data object Post : Result()
        data object Comment : Result()
    }
}

sealed class CommunityIntent {
    data object LoadInitialPosts : CommunityIntent()
    data object LoadMorePosts : CommunityIntent()
    data object Refresh : CommunityIntent()
    data class FilterByType(val type: String?) : CommunityIntent()
    data class FilterByTopic(val topic: String?) : CommunityIntent()
    data class ChangeSort(val sort: String) : CommunityIntent()
    data class NavigateToPostDetail(val postId: Long) : CommunityIntent()
    data class NavigateToNotice(val noticeId: Long) : CommunityIntent()
    data class ToggleLike(val postId: Long) : CommunityIntent()
    data object NavigateToSearch : CommunityIntent()
    data class ChangeSearchScreenState(val state: CommunityScreenState) : CommunityIntent()
    data object NavigateToNotifications : CommunityIntent()
    data object NavigateToCreatePost : CommunityIntent()
}

sealed class CommunityEvent {
    sealed class DataFetch : CommunityEvent() {
        data class Error(
            override val userMessage: String = "게시글을 불러올 수 없습니다",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }
    
    data class NavigateToPostDetail(val postId: Long) : CommunityEvent()
    data class NavigateToNotice(val noticeId: Long) : CommunityEvent()
    data class ShowComingSoonMessage(val feature: String) : CommunityEvent()
    data object NavigateToWritePost : CommunityEvent()
}