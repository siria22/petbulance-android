package com.petbulance.presentation.screen.feature.mypage.sections.activity.posts

import androidx.lifecycle.SavedStateHandle
import com.petbulance.domain.model.feature.community.post.MyPostSummary
import com.petbulance.domain.usecase.feature.community.post.DeleteMyPostsUseCase
import com.petbulance.domain.usecase.feature.community.post.GetMyPostListUseCase
import com.petbulance.presentation.utils.BaseViewModel
import com.petbulance.presentation.utils.error.ErrorDisplayType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MyPagePostsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMyPostListUseCase: GetMyPostListUseCase,
    private val deleteMyPostsUseCase: DeleteMyPostsUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<MyPagePostsDataState>(MyPagePostsDataState.Init)
    val dataState: StateFlow<MyPagePostsDataState> = _dataState.asStateFlow()

    private val _screenState =
        MutableStateFlow<MyPagePostsScreenState>(MyPagePostsScreenState.Normal())
    val screenState: StateFlow<MyPagePostsScreenState> = _screenState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<MyPagePostsEvent>()
    val eventFlow: SharedFlow<MyPagePostsEvent> = _eventFlow

    private val currentPosts = mutableListOf<MyPostSummary>()

    init {
        observeErrorEvent(eventFlow)
        onIntent(MyPagePostsIntent.LoadData)
    }

    fun onIntent(intent: MyPagePostsIntent) {
        when (intent) {
            is MyPagePostsIntent.LoadData -> loadPosts(isRefresh = false)
            is MyPagePostsIntent.Refresh -> loadPosts(isRefresh = true)
            is MyPagePostsIntent.LoadMore -> loadMorePosts()
            is MyPagePostsIntent.ToggleSelectionMode -> toggleSelectionMode(intent.enabled)
            is MyPagePostsIntent.TogglePostSelection -> togglePostSelection(intent.postId)
            is MyPagePostsIntent.SelectAll -> selectAll()
            is MyPagePostsIntent.DeleteSelected -> deleteSelectedPosts()
        }
    }

    private fun loadPosts(isRefresh: Boolean) {
        launch {
            if (!isRefresh) _dataState.value = MyPagePostsDataState.Loading

            if (isRefresh) {
                _screenState.update { MyPagePostsScreenState.Normal() }
                currentPosts.clear()
            }

            getMyPostListUseCase(lastPostId = null, pageSize = 20)
                .onSuccess { result ->
                    currentPosts.addAll(result.items)
                    _dataState.value = MyPagePostsDataState.Loaded(
                        posts = currentPosts.toList(),
                        hasNext = result.hasNext
                    )
                }
                .onFailure { handleFetchError(it) }
        }
    }

    private fun loadMorePosts() {
        val currentState = _dataState.value
        if (currentState !is MyPagePostsDataState.Loaded || !currentState.hasNext) return

        launch {
            val lastPostId = currentPosts.lastOrNull()?.postId

            getMyPostListUseCase(lastPostId = lastPostId, pageSize = 20)
                .onSuccess { result ->
                    currentPosts.addAll(result.items)
                    _dataState.value = currentState.copy(
                        posts = currentPosts.toList(),
                        hasNext = result.hasNext
                    )
                }
                .onFailure { handleFetchError(it) }
        }
    }

    private fun toggleSelectionMode(enabled: Boolean) {
        _screenState.update {
            MyPagePostsScreenState.Normal(isSelectionMode = enabled, selectedIds = emptySet())
        }
    }

    private fun togglePostSelection(postId: Long) {
        _screenState.update { state ->
            if (state is MyPagePostsScreenState.Normal) {
                val newSelection = state.selectedIds.toMutableSet()
                if (newSelection.contains(postId)) newSelection.remove(postId)
                else newSelection.add(postId)
                state.copy(selectedIds = newSelection)
            } else state
        }
    }

    private fun selectAll() {
        _screenState.update { state ->
            if (state is MyPagePostsScreenState.Normal) {
                val allIds = currentPosts.map { it.postId }.toSet()
                if (state.selectedIds.size == allIds.size && allIds.isNotEmpty()) {
                    state.copy(selectedIds = emptySet())
                } else {
                    state.copy(selectedIds = allIds)
                }
            } else state
        }
    }

    private fun deleteSelectedPosts() {
        val state = _screenState.value as? MyPagePostsScreenState.Normal ?: return
        val targetIds = state.selectedIds.toList()

        if (targetIds.isEmpty()) return

        launch {
            _dataState.value = MyPagePostsDataState.Loading

            deleteMyPostsUseCase(targetIds)
                .onSuccess {
                    _eventFlow.emit(MyPagePostsEvent.Post.DeleteSuccess)
                    loadPosts(isRefresh = true)
                }
                .onFailure { exception ->
                    _eventFlow.emit(
                        MyPagePostsEvent.Post.DeleteFailed(
                            displayType = ErrorDisplayType.Common,
                            userMessage = "삭제 처리에 실패했습니다.",
                            exceptionMessage = exception.message
                        )
                    )
                    loadPosts(isRefresh = true)
                }
        }
    }

    private suspend fun handleFetchError(exception: Throwable) {
        _eventFlow.emit(
            MyPagePostsEvent.DataFetch.Error(
                displayType = ErrorDisplayType.Common,
                userMessage = "게시글 목록을 불러오지 못했습니다.",
                exceptionMessage = exception.message
            )
        )

        if (_dataState.value is MyPagePostsDataState.Loading) {
            _dataState.value = MyPagePostsDataState.Init
        }
    }
}
