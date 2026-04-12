package com.petbulance.presentation.screen.feature.community

import androidx.lifecycle.SavedStateHandle
import com.petbulance.domain.model.feature.community.post.PostSummary
import com.petbulance.domain.usecase.feature.community.post.GetPostListUseCase
import com.petbulance.domain.usecase.feature.community.post.TogglePostLikeUseCase
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
    private val getPostListUseCase: GetPostListUseCase,
    private val togglePostLikeUseCase: TogglePostLikeUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<CommunityDataState>(CommunityDataState.Init)
    val dataState: StateFlow<CommunityDataState> = _dataState

    private val _screenState = MutableStateFlow<CommunityScreenState>(CommunityScreenState.Home)
    val screenState: StateFlow<CommunityScreenState> = _screenState

    private val _eventFlow = MutableSharedFlow<CommunityEvent>(extraBufferCapacity = 1)
    val eventFlow: SharedFlow<CommunityEvent> = _eventFlow

    private val _communityData = MutableStateFlow(CommunityData.empty)
    val communityData: StateFlow<CommunityData> = _communityData

    private var currentPageCount = 0

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
        val data = _communityData.value

        launch {
            _dataState.update { CommunityDataState.Loading }
            currentPageCount = 0

            getPostListUseCase(
                type = data.currentType,
                topic = data.currentTopic,
                sort = data.currentSort,
                lastPostId = null,
                pageSize = PAGE_SIZE
            ).onSuccess { pagingPostList ->
                _communityData.update {
                    it.copy(
                        noticeBanner = pagingPostList.noticeBanner,
                        posts = pagingPostList.items,
                        hasNext = pagingPostList.hasNext
                    )
                }
                currentPageCount = 1
            }.onFailure { exception ->
                emitError(exception)
            }

            _dataState.update { CommunityDataState.Init }
        }
    }

    private fun loadMorePosts() {
        if (_dataState.value == CommunityDataState.LoadingMore) return
        val data = _communityData.value
        if (!data.hasNext) return

        // TODO: [정책 확인 필요] 비로그인 사용자 페이징 제한 - PM과 논의 후 최종 결정
        if (currentPageCount >= MAX_NON_LOGIN_PAGES) return

        launch {
            _dataState.update { CommunityDataState.LoadingMore }

            val lastPostId = data.posts.lastOrNull()?.id

            getPostListUseCase(
                type = data.currentType,
                topic = data.currentTopic,
                sort = data.currentSort,
                lastPostId = lastPostId,
                pageSize = PAGE_SIZE
            ).onSuccess { pagingPostList ->
                _communityData.update {
                    it.copy(
                        posts = it.posts + pagingPostList.items,
                        hasNext = pagingPostList.hasNext
                    )
                }
                currentPageCount++
            }.onFailure { exception ->
                emitError(exception)
            }

            _dataState.update { CommunityDataState.Init }
        }
    }

    private fun refreshPosts() {
        _communityData.update { it.copy(posts = emptyList(), hasNext = false) }
        loadInitialPosts()
    }

    private fun silentRefresh() {
        launch {
            val data = _communityData.value
            val currentPostCount = data.posts.size
            if (currentPostCount == 0) {
                loadInitialPosts()
                return@launch
            }

            val pagesToLoad = (currentPostCount / PAGE_SIZE) + 1
            val allPosts = mutableListOf<PostSummary>()
            var lastPostId: Long? = null
            var hasMorePages = true

            for (page in 1..pagesToLoad) {
                if (!hasMorePages) break

                getPostListUseCase(
                    type = data.currentType,
                    topic = data.currentTopic,
                    sort = data.currentSort,
                    lastPostId = lastPostId,
                    pageSize = PAGE_SIZE
                ).onSuccess { pagingPostList ->
                    if (page == 1) {
                        _communityData.update { it.copy(noticeBanner = pagingPostList.noticeBanner) }
                    }
                    allPosts.addAll(pagingPostList.items)
                    lastPostId = pagingPostList.items.lastOrNull()?.id
                    hasMorePages = pagingPostList.hasNext
                }.onFailure {
                    hasMorePages = false
                }
            }

            if (allPosts.isNotEmpty()) {
                _communityData.update {
                    it.copy(posts = allPosts, hasNext = hasMorePages)
                }
            }
        }
    }

    private fun filterByType(type: String?) {
        _communityData.update { it.copy(currentType = type) }
        refreshPosts()
    }

    private fun filterByTopic(topic: String?) {
        _communityData.update { it.copy(currentTopic = topic) }
        refreshPosts()
    }

    private fun changeSort(sort: String) {
        _communityData.update { it.copy(currentSort = sort) }
        refreshPosts()
    }

    private fun navigateToPostDetail(postId: Long) {
        _eventFlow.tryEmit(CommunityEvent.NavigateToPostDetail(postId))
    }

    private fun navigateToNotice(noticeId: Long) {
        _eventFlow.tryEmit(CommunityEvent.NavigateToNotice(noticeId))
    }

    private fun toggleLike(postId: Long) {
        val currentPost = _communityData.value.posts.find { it.id == postId } ?: return

        // 낙관적 UI 업데이트
        _communityData.update { data ->
            data.copy(
                posts = data.posts.map { post ->
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

        launch {
            togglePostLikeUseCase(postId, currentPost.isLiked)
                .onSuccess { postLike ->
                    // 서버 응답으로 정확한 값 반영
                    _communityData.update { data ->
                        data.copy(
                            posts = data.posts.map { post ->
                                if (post.id == postId) {
                                    post.copy(
                                        isLiked = postLike.isLiked,
                                        likeCount = postLike.currentLikeCount.toInt()
                                    )
                                } else {
                                    post
                                }
                            }
                        )
                    }
                }
                .onFailure {
                    // 실패 시 원래 상태로 롤백
                    _communityData.update { data ->
                        data.copy(
                            posts = data.posts.map { post ->
                                if (post.id == postId) {
                                    post.copy(
                                        isLiked = currentPost.isLiked,
                                        likeCount = currentPost.likeCount
                                    )
                                } else {
                                    post
                                }
                            }
                        )
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
        _eventFlow.tryEmit(CommunityEvent.NavigateToWritePost)
    }

    private fun showComingSoon(feature: String) {
        _eventFlow.tryEmit(CommunityEvent.ShowComingSoonMessage(feature))
    }

    private fun emitError(exception: Throwable) {
        _eventFlow.tryEmit(
            CommunityEvent.DataFetch.Error(
                displayType = ErrorDisplayType.Common,
                userMessage = "게시글을 불러올 수 없습니다",
                exceptionMessage = exception.message
            )
        )
    }

    companion object {
        private const val PAGE_SIZE = 10
        private const val MAX_NON_LOGIN_PAGES = 3
    }
}
