package com.petbulance.presentation.screen.feature.community

import androidx.lifecycle.SavedStateHandle
import com.petbulance.domain.model.feature.community.post.NoticeBanner
import com.petbulance.domain.model.feature.community.post.PostSummary
import com.petbulance.domain.usecase.feature.community.post.GetPostListUseCase
import com.petbulance.presentation.utils.BaseViewModel
import com.petbulance.presentation.utils.error.ErrorDisplayType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getPostListUseCase: GetPostListUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<CommunityDataState>(CommunityDataState.Init)
    val dataState: StateFlow<CommunityDataState> = _dataState

    private val _screenState = MutableStateFlow<CommunityScreenState>(CommunityScreenState.Home)
    val screenState: StateFlow<CommunityScreenState> = _screenState

    private val _eventFlow = MutableSharedFlow<CommunityEvent>(extraBufferCapacity = 1)
    val eventFlow: SharedFlow<CommunityEvent> = _eventFlow

    private val _noticeBanner = MutableStateFlow<NoticeBanner?>(null)
    val noticeBanner: StateFlow<NoticeBanner?> = _noticeBanner

    private val _posts = MutableStateFlow<List<PostSummary>>(emptyList())
    val posts: StateFlow<List<PostSummary>> = _posts

    private val _hasNext = MutableStateFlow(false)
    val hasNext: StateFlow<Boolean> = _hasNext

    private val _currentType = MutableStateFlow<String?>(null)
    val currentType: StateFlow<String?> = _currentType

    private val _currentTopic = MutableStateFlow<String?>(null)
    val currentTopic: StateFlow<String?> = _currentTopic

    private val _currentSort = MutableStateFlow("latest")
    val currentSort: StateFlow<String> = _currentSort

    private var currentPageCount = 0
    private val maxNonLoginPages = 3

    fun onIntent(intent: CommunityIntent) {
        when (intent) {
            is CommunityIntent.LoadInitialPosts -> loadInitialPosts()
            is CommunityIntent.LoadMorePosts -> loadMorePosts()
            is CommunityIntent.Refresh -> refreshPosts()
            is CommunityIntent.SilentRefresh -> silentRefresh()
            is CommunityIntent.FilterByType -> filterByType(intent.type)
            is CommunityIntent.FilterByTopic -> filterByTopic(intent.topic)
            is CommunityIntent.ChangeSort -> changeSort(intent.sort)
            is CommunityIntent.NavigateToPostDetail -> navigateToPostDetail(intent.postId)
            is CommunityIntent.NavigateToNotice -> navigateToNotice(intent.noticeId)
            is CommunityIntent.ToggleLike -> toggleLike(intent.postId)
            is CommunityIntent.NavigateToSearch -> navigateToSearch()
            is CommunityIntent.ChangeSearchScreenState -> changeSearchScreenState(intent.state)
            is CommunityIntent.NavigateToNotifications -> showComingSoon("알림")
            is CommunityIntent.NavigateToCreatePost -> navigateToCreatePost()
        }
    }

    init {
        observeErrorEvent(eventFlow)
        loadInitialPosts()
    }

    private fun loadInitialPosts() {
        if (_dataState.value == CommunityDataState.Loading) return

        launch {
            _dataState.update { CommunityDataState.Loading }
            currentPageCount = 0

            runCatching {
                getPostListUseCase(
                    type = _currentType.value,
                    topic = _currentTopic.value,
                    sort = _currentSort.value,
                    lastPostId = null,
                    pageSize = 10
                )
            }.onSuccess { result ->
                result.onSuccess { pagingPostList ->
                    _noticeBanner.update { pagingPostList.noticeBanner }
                    _posts.update { pagingPostList.items }
                    _hasNext.update { pagingPostList.hasNext }
                    currentPageCount = 1
                }.onFailure { exception ->
                    _eventFlow.tryEmit(
                        CommunityEvent.DataFetch.Error(
                            displayType = ErrorDisplayType.Common,
                            userMessage = "게시글을 불러올 수 없습니다",
                            exceptionMessage = exception.message
                        )
                    )
                }
            }.onFailure { exception ->
                _eventFlow.tryEmit(
                    CommunityEvent.DataFetch.Error(
                        displayType = ErrorDisplayType.Common,
                        userMessage = "게시글을 불러올 수 없습니다",
                        exceptionMessage = exception.message
                    )
                )
            }

            _dataState.update { CommunityDataState.Init }
        }
    }

    private fun loadMorePosts() {
        if (_dataState.value == CommunityDataState.LoadingMore) return
        if (!_hasNext.value) return
        
        // TODO: [정책 확인 필요] 비로그인 사용자 페이징 제한 - PM과 논의 후 최종 결정
        if (currentPageCount >= maxNonLoginPages) {
            // 로그인 유도 로직 추가 가능
            return
        }

        launch {
            _dataState.update { CommunityDataState.LoadingMore }

            val lastPostId = _posts.value.lastOrNull()?.id

            runCatching {
                getPostListUseCase(
                    type = _currentType.value,
                    topic = _currentTopic.value,
                    sort = _currentSort.value,
                    lastPostId = lastPostId,
                    pageSize = 10
                )
            }.onSuccess { result ->
                result.onSuccess { pagingPostList ->
                    _posts.update { currentPosts -> currentPosts + pagingPostList.items }
                    _hasNext.update { pagingPostList.hasNext }
                    currentPageCount++
                }.onFailure { exception ->
                    _eventFlow.tryEmit(
                        CommunityEvent.DataFetch.Error(
                            displayType = ErrorDisplayType.Common,
                            userMessage = "게시글을 불러올 수 없습니다",
                            exceptionMessage = exception.message
                        )
                    )
                }
            }.onFailure { exception ->
                _eventFlow.tryEmit(
                    CommunityEvent.DataFetch.Error(
                        displayType = ErrorDisplayType.Common,
                        userMessage = "게시글을 불러올 수 없습니다",
                        exceptionMessage = exception.message
                    )
                )
            }

            _dataState.update { CommunityDataState.Init }
        }
    }

    private fun refreshPosts() {
        _posts.update { emptyList() }
        _hasNext.update { false }
        loadInitialPosts()
    }

    private fun silentRefresh() {
        launch {
            val currentPostCount = _posts.value.size
            if (currentPostCount == 0) {
                loadInitialPosts()
                return@launch
            }

            val pagesToLoad = (currentPostCount / 10) + 1
            val allPosts = mutableListOf<com.petbulance.domain.model.feature.community.post.PostSummary>()
            var lastPostId: Long? = null
            var hasMorePages = true

            for (page in 1..pagesToLoad) {
                if (!hasMorePages) break

                runCatching {
                    getPostListUseCase(
                        type = _currentType.value,
                        topic = _currentTopic.value,
                        sort = _currentSort.value,
                        lastPostId = lastPostId,
                        pageSize = 10
                    )
                }.onSuccess { result ->
                    result.onSuccess { pagingPostList ->
                        if (page == 1) {
                            _noticeBanner.update { pagingPostList.noticeBanner }
                        }
                        allPosts.addAll(pagingPostList.items)
                        lastPostId = pagingPostList.items.lastOrNull()?.id
                        hasMorePages = pagingPostList.hasNext
                    }.onFailure {
                        hasMorePages = false
                    }
                }.onFailure {
                    hasMorePages = false
                }
            }

            if (allPosts.isNotEmpty()) {
                _posts.update { allPosts }
                _hasNext.update { hasMorePages }
            }
        }
    }

    private fun filterByType(type: String?) {
        _currentType.update { type }
        refreshPosts()
    }

    private fun filterByTopic(topic: String?) {
        _currentTopic.update { topic }
        refreshPosts()
    }

    private fun changeSort(sort: String) {
        _currentSort.update { sort }
        refreshPosts()
    }

    private fun navigateToPostDetail(postId: Long) {
        launch {
            _eventFlow.tryEmit(CommunityEvent.NavigateToPostDetail(postId))
        }
    }

    private fun navigateToNotice(noticeId: Long) {
        launch {
            _eventFlow.tryEmit(CommunityEvent.NavigateToNotice(noticeId))
        }
    }

    private fun toggleLike(postId: Long) {
        // TODO: [구현 필요] 좋아요 토글 로직 - LikePostUseCase 필요
        launch {
            // 임시로 로컬 상태만 업데이트
            _posts.update { currentPosts ->
                currentPosts.map { post ->
                    if (post.id == postId) {
                        post.copy(
                            isLiked = !post.isLiked,
                            likeCount = if (post.isLiked) post.likeCount - 1 else post.likeCount + 1
                        )
                    } else {
                        post
                    }
                }
            }
        }
    }

    private fun navigateToSearch() {
        _screenState.update { CommunityScreenState.Search }
    }

    private fun changeSearchScreenState(state: CommunityScreenState) {
        _screenState.update { state }
    }

    private fun navigateToCreatePost() {
        launch {
            _eventFlow.tryEmit(CommunityEvent.NavigateToWritePost)
        }
    }

    private fun showComingSoon(feature: String) {
        launch {
            _eventFlow.tryEmit(CommunityEvent.ShowComingSoonMessage(feature))
        }
    }
}
