package com.petbulance.presentation.screen.feature.community.search

import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class CommunitySearchArgument(
    val intent: (CommunitySearchIntent) -> Unit,
    val dataState: CommunitySearchDataState,
    val event: SharedFlow<CommunitySearchEvent>
)

sealed class CommunitySearchDataState {
    data object Init : CommunitySearchDataState()
    data object Loading : CommunitySearchDataState()
    data object LoadingMore : CommunitySearchDataState()
}

sealed class CommunitySearchIntent {
    data class SearchPosts(val keyword: String) : CommunitySearchIntent()
    data class SearchComments(val keyword: String) : CommunitySearchIntent()
    data object LoadMorePosts : CommunitySearchIntent()
    data object LoadMoreComments : CommunitySearchIntent()
    data class ChangeSort(val sort: String) : CommunitySearchIntent()
    data class ChangeSearchScope(val scope: String) : CommunitySearchIntent()
    data class ApplyFilter(val animalCategory: String?, val postCategory: String?) : CommunitySearchIntent()
    data object ClearFilter : CommunitySearchIntent()
    data class NavigateToPostDetail(val postId: Long, val commentId: Long? = null) : CommunitySearchIntent()
    data class DeleteRecentKeyword(val keyword: String) : CommunitySearchIntent()
    data object DeleteAllRecentKeywords : CommunitySearchIntent()
    data class RestoreRecentKeywords(val keywords: List<String>) : CommunitySearchIntent()
}

sealed class CommunitySearchEvent {
    sealed class DataFetch : CommunitySearchEvent() {
        data class Error(
            override val userMessage: String = "검색 결과를 불러올 수 없습니다",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }
    
    data class NavigateToPostDetail(val postId: Long, val commentId: Long? = null) : CommunitySearchEvent()
    data class ChangeScreenState(val state: com.petbulance.presentation.screen.feature.community.CommunityScreenState) : CommunitySearchEvent()
}
