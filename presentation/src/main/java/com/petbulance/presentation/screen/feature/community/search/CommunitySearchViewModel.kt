package com.petbulance.presentation.screen.feature.community.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.petbulance.domain.usecase.feature.community.comment.SearchPostCommentListUseCase
import com.petbulance.domain.usecase.feature.community.post.GetPostSearchListUseCase
import com.petbulance.domain.usecase.feature.community.search.AddRecentSearchKeywordUseCase
import com.petbulance.domain.usecase.feature.community.search.DeleteAllRecentSearchKeywordsUseCase
import com.petbulance.domain.usecase.feature.community.search.DeleteRecentSearchKeywordUseCase
import com.petbulance.domain.usecase.feature.community.search.GetRecentSearchKeywordsUseCase
import com.petbulance.presentation.screen.feature.community.CommunityScreenState
import com.petbulance.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CommunitySearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getPostSearchListUseCase: GetPostSearchListUseCase,
    private val searchPostCommentListUseCase: SearchPostCommentListUseCase,
    private val getRecentSearchKeywordsUseCase: GetRecentSearchKeywordsUseCase,
    private val addRecentSearchKeywordUseCase: AddRecentSearchKeywordUseCase,
    private val deleteRecentSearchKeywordUseCase: DeleteRecentSearchKeywordUseCase,
    private val deleteAllRecentSearchKeywordsUseCase: DeleteAllRecentSearchKeywordsUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<CommunitySearchDataState>(CommunitySearchDataState.Init)
    val dataState: StateFlow<CommunitySearchDataState> = _dataState.asStateFlow()

    private val _data = MutableStateFlow(CommunitySearchData.empty)
    val data: StateFlow<CommunitySearchData> = _data.asStateFlow()

    private val _eventFlow = MutableSharedFlow<CommunitySearchEvent>()
    val eventFlow: SharedFlow<CommunitySearchEvent> = _eventFlow.asSharedFlow()

    private val _recentKeywords = MutableStateFlow<List<String>>(emptyList())
    val recentKeywords: StateFlow<List<String>> = _recentKeywords.asStateFlow()

    private var lastPostId: Long? = null
    private var lastCommentId: Long? = null
    
    private val maxNonLoginPages = 3
    private var currentPostPage = 0
    private var currentCommentPage = 0

    init {
        observeErrorEvent(eventFlow)
        loadRecentKeywords()
    }

    private fun loadRecentKeywords() {
        viewModelScope.launch {
            getRecentSearchKeywordsUseCase().collect { keywords ->
                _recentKeywords.value = keywords.map { it.keyword }
            }
        }
    }

    fun onIntent(intent: CommunitySearchIntent) {
        when (intent) {
            is CommunitySearchIntent.SearchPosts -> searchPosts(intent.keyword)
            is CommunitySearchIntent.SearchComments -> searchComments(intent.keyword)
            is CommunitySearchIntent.LoadMorePosts -> loadMorePosts()
            is CommunitySearchIntent.LoadMoreComments -> loadMoreComments()
            is CommunitySearchIntent.ChangeSort -> changeSort(intent.sort)
            is CommunitySearchIntent.ChangeSearchScope -> changeSearchScope(intent.scope)
            is CommunitySearchIntent.ApplyFilter -> applyFilter(intent.animalCategory, intent.postCategory)
            is CommunitySearchIntent.ClearFilter -> clearFilter()
            is CommunitySearchIntent.ToggleLike -> toggleLike(intent.postId)
            is CommunitySearchIntent.NavigateToPostDetail -> navigateToPostDetail(intent.postId, intent.commentId)
            is CommunitySearchIntent.DeleteRecentKeyword -> deleteRecentKeyword(intent.keyword)
            is CommunitySearchIntent.DeleteAllRecentKeywords -> deleteAllRecentKeywords()
            is CommunitySearchIntent.RestoreRecentKeywords -> restoreRecentKeywords(intent.keywords)
        }
    }

    private fun searchPosts(keyword: String) {
        val trimmedKeyword = keyword.trim()
        if (trimmedKeyword.isEmpty()) {
            return
        }

        launch {
            addRecentSearchKeywordUseCase(trimmedKeyword)
            
            _dataState.value = CommunitySearchDataState.Loading
            _data.update { it.copy(searchKeyword = trimmedKeyword, postResults = emptyList()) }
            
            // 화면 상태 변경 이벤트 발생
            _eventFlow.emit(CommunitySearchEvent.ChangeScreenState(CommunityScreenState.Result.Post))
            
            lastPostId = null
            currentPostPage = 0

            val result = getPostSearchListUseCase(
                type = _data.value.selectedAnimalCategory,
                topic = _data.value.selectedPostCategory,
                sort = _data.value.currentSort,
                lastPostId = null,
                pageSize = 20,
                searchKeyword = trimmedKeyword,
                searchScope = _data.value.searchScope
            )

            result.fold(
                onSuccess = { response ->
                    currentPostPage = 1
                    lastPostId = response.items.lastOrNull()?.id
                    _data.update {
                        it.copy(
                            postResults = response.items,
                            hasNextPost = response.hasNext,
                            totalPostCount = response.totalPostCount
                        )
                    }
                    _dataState.value = CommunitySearchDataState.Init
                },
                onFailure = { error ->
                    _dataState.value = CommunitySearchDataState.Init
                    _eventFlow.emit(
                        CommunitySearchEvent.DataFetch.Error(
                            exceptionMessage = error.message
                        )
                    )
                }
            )
        }
    }

    private fun searchComments(keyword: String) {
        val trimmedKeyword = keyword.trim()
        if (trimmedKeyword.isEmpty()) {
            return
        }

        launch {
            addRecentSearchKeywordUseCase(trimmedKeyword)
            
            _dataState.value = CommunitySearchDataState.Loading
            _data.update { it.copy(searchKeyword = trimmedKeyword, commentResults = emptyList()) }
            
            // 화면 상태 변경 이벤트 발생
            _eventFlow.emit(CommunitySearchEvent.ChangeScreenState(CommunityScreenState.Result.Comment))
            
            lastCommentId = null
            currentCommentPage = 0

            val topic = _data.value.selectedPostCategory
            val type = _data.value.selectedAnimalCategory

            val result = searchPostCommentListUseCase(
                searchKeyword = trimmedKeyword,
                searchScope = _data.value.getCommentSearchScope(),
                lastCommentId = null,
                pageSize = 20,
                topic = topic,
                type = type
            )

            result.fold(
                onSuccess = { response ->
                    currentCommentPage = 1
                    lastCommentId = response.content.lastOrNull()?.commentId
                    _data.update {
                        it.copy(
                            commentResults = response.content,
                            hasNextComment = response.hasNext,
                            totalCommentCount = response.totalCount
                        )
                    }
                    _dataState.value = CommunitySearchDataState.Init
                },
                onFailure = { error ->
                    _dataState.value = CommunitySearchDataState.Init
                    _eventFlow.emit(
                        CommunitySearchEvent.DataFetch.Error(
                            exceptionMessage = error.message
                        )
                    )
                }
            )
        }
    }

    private fun loadMorePosts() {
        if (!_data.value.hasNextPost || _dataState.value is CommunitySearchDataState.LoadingMore) {
            return
        }

        if (currentPostPage >= maxNonLoginPages) {
            return
        }

        launch {
            _dataState.value = CommunitySearchDataState.LoadingMore

            val result = getPostSearchListUseCase(
                type = _data.value.selectedAnimalCategory,
                topic = _data.value.selectedPostCategory,
                sort = _data.value.currentSort,
                lastPostId = lastPostId,
                pageSize = 20,
                searchKeyword = _data.value.searchKeyword,
                searchScope = _data.value.searchScope
            )

            result.fold(
                onSuccess = { response ->
                    currentPostPage++
                    lastPostId = response.items.lastOrNull()?.id
                    _data.update {
                        it.copy(
                            postResults = it.postResults + response.items,
                            hasNextPost = response.hasNext
                        )
                    }
                    _dataState.value = CommunitySearchDataState.Init
                },
                onFailure = { error ->
                    _dataState.value = CommunitySearchDataState.Init
                    _eventFlow.emit(
                        CommunitySearchEvent.DataFetch.Error(
                            exceptionMessage = error.message
                        )
                    )
                }
            )
        }
    }

    private fun loadMoreComments() {
        if (!_data.value.hasNextComment || _dataState.value is CommunitySearchDataState.LoadingMore) {
            return
        }

        if (currentCommentPage >= maxNonLoginPages) {
            return
        }

        launch {
            _dataState.value = CommunitySearchDataState.LoadingMore

            val topic = _data.value.selectedPostCategory
            val type = _data.value.selectedAnimalCategory

            val result = searchPostCommentListUseCase(
                searchKeyword = _data.value.searchKeyword,
                searchScope = _data.value.getCommentSearchScope(),
                lastCommentId = lastCommentId,
                pageSize = 20,
                topic = topic,
                type = type
            )

            result.fold(
                onSuccess = { response ->
                    currentCommentPage++
                    lastCommentId = response.content.lastOrNull()?.commentId
                    _data.update {
                        it.copy(
                            commentResults = it.commentResults + response.content,
                            hasNextComment = response.hasNext
                        )
                    }
                    _dataState.value = CommunitySearchDataState.Init
                },
                onFailure = { error ->
                    _dataState.value = CommunitySearchDataState.Init
                    _eventFlow.emit(
                        CommunitySearchEvent.DataFetch.Error(
                            exceptionMessage = error.message
                        )
                    )
                }
            )
        }
    }

    private fun changeSort(sort: String) {
        _data.update { it.copy(currentSort = sort) }
        searchPosts(_data.value.searchKeyword)
    }

    private fun changeSearchScope(scope: String) {
        _data.update { it.copy(searchScope = scope) }
    }

    private fun applyFilter(animalCategory: String?, postCategory: String?) {
        _data.update {
            it.copy(
                selectedAnimalCategory = animalCategory,
                selectedPostCategory = postCategory
            )
        }
        searchPosts(_data.value.searchKeyword)
    }

    private fun clearFilter() {
        _data.update {
            it.copy(
                selectedAnimalCategory = null,
                selectedPostCategory = null
            )
        }
        searchPosts(_data.value.searchKeyword)
    }

    private fun deleteRecentKeyword(keyword: String) {
        launch {
            deleteRecentSearchKeywordUseCase(keyword)
        }
    }

    private fun deleteAllRecentKeywords() {
        launch {
            deleteAllRecentSearchKeywordsUseCase()
        }
    }

    private fun restoreRecentKeywords(keywords: List<String>) {
        launch {
            keywords.forEach { keyword ->
                addRecentSearchKeywordUseCase(keyword)
            }
        }
    }

    private fun toggleLike(postId: Long) {
        launch {
            _data.update { currentData ->
                currentData.copy(
                    postResults = currentData.postResults.map { post ->
                        if (post.id == postId) {
                            post.copy(
                                isLiked = !post.isLiked,
                                likeCount = if (post.isLiked) post.likeCount - 1 else post.likeCount + 1
                            )
                        } else {
                            post
                        }
                    }
                )
            }
        }
    }

    private fun navigateToPostDetail(postId: Long, commentId: Long?) {
        launch {
            _eventFlow.emit(CommunitySearchEvent.NavigateToPostDetail(postId, commentId))
        }
    }

    init {
        observeErrorEvent(eventFlow)
    }
}
