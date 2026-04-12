package com.petbulance.presentation.screen.feature.mypage.sections.activity.comments

import androidx.lifecycle.SavedStateHandle
import com.petbulance.domain.model.feature.community.comment.MyCommentListRes
import com.petbulance.domain.usecase.feature.community.comment.DeleteMyCommentsUseCase
import com.petbulance.domain.usecase.feature.community.comment.GetMyCommentListUseCase
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
class MyPageCommentsViewModel @Inject constructor(
    private val getMyCommentListUseCase: GetMyCommentListUseCase,
    private val deleteMyCommentsUseCase: DeleteMyCommentsUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<MyPageCommentsDataState>(MyPageCommentsDataState.Init)
    val dataState: StateFlow<MyPageCommentsDataState> = _dataState.asStateFlow()

    private val _screenState =
        MutableStateFlow<MyPageCommentsScreenState>(MyPageCommentsScreenState.Normal())
    val screenState: StateFlow<MyPageCommentsScreenState> = _screenState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<MyPageCommentsEvent>()
    val eventFlow: SharedFlow<MyPageCommentsEvent> = _eventFlow

    private val currentComments = mutableListOf<MyCommentListRes>()

    init {
        observeErrorEvent(eventFlow)
        onIntent(MyPageCommentsIntent.LoadData)
    }

    fun onIntent(intent: MyPageCommentsIntent) {
        when (intent) {
            is MyPageCommentsIntent.LoadData -> loadComments(isRefresh = false)
            is MyPageCommentsIntent.Refresh -> loadComments(isRefresh = true)
            is MyPageCommentsIntent.LoadMore -> loadMoreComments()
            is MyPageCommentsIntent.ToggleSelectionMode -> toggleSelectionMode(intent.enabled)
            is MyPageCommentsIntent.ToggleCommentSelection -> toggleCommentSelection(intent.commentId)
            is MyPageCommentsIntent.SelectAll -> selectAll()
            is MyPageCommentsIntent.DeleteSelected -> deleteSelectedComments()
        }
    }

    private fun loadComments(isRefresh: Boolean) {
        launch {
            if (!isRefresh) _dataState.value = MyPageCommentsDataState.Loading

            if (isRefresh) {
                _screenState.update { MyPageCommentsScreenState.Normal() }
                currentComments.clear()
            }

            getMyCommentListUseCase(lastCommentId = null, pageSize = 20)
                .onSuccess { result ->
                    currentComments.addAll(result.items)
                    _dataState.value = MyPageCommentsDataState.Loaded(
                        comments = currentComments.toList(),
                        hasNext = result.hasNext
                    )
                }
                .onFailure { handleFetchError(it) }
        }
    }

    private fun loadMoreComments() {
        val currentState = _dataState.value
        if (currentState !is MyPageCommentsDataState.Loaded || !currentState.hasNext) return

        launch {
            val lastCommentId = currentComments.lastOrNull()?.commentId

            getMyCommentListUseCase(lastCommentId = lastCommentId, pageSize = 20)
                .onSuccess { result ->
                    currentComments.addAll(result.items)
                    _dataState.value = currentState.copy(
                        comments = currentComments.toList(),
                        hasNext = result.hasNext
                    )
                }
                .onFailure { handleFetchError(it) }
        }
    }

    private fun toggleSelectionMode(enabled: Boolean) {
        _screenState.update {
            MyPageCommentsScreenState.Normal(isSelectionMode = enabled, selectedIds = emptySet())
        }
    }

    private fun toggleCommentSelection(commentId: Long) {
        _screenState.update { state ->
            if (state is MyPageCommentsScreenState.Normal) {
                val newSelection = state.selectedIds.toMutableSet()
                if (newSelection.contains(commentId)) newSelection.remove(commentId)
                else newSelection.add(commentId)
                state.copy(selectedIds = newSelection)
            } else state
        }
    }

    private fun selectAll() {
        _screenState.update { state ->
            if (state is MyPageCommentsScreenState.Normal) {
                val allIds = currentComments.map { it.commentId }.toSet()
                if (state.selectedIds.size == allIds.size && allIds.isNotEmpty()) {
                    state.copy(selectedIds = emptySet())
                } else {
                    state.copy(selectedIds = allIds)
                }
            } else state
        }
    }

    private fun deleteSelectedComments() {
        val state = _screenState.value as? MyPageCommentsScreenState.Normal ?: return
        val targetIds = state.selectedIds.toList()

        if (targetIds.isEmpty()) return

        launch {
            _dataState.value = MyPageCommentsDataState.Loading

            deleteMyCommentsUseCase(targetIds)
                .onSuccess {
                    _eventFlow.emit(MyPageCommentsEvent.Comment.DeleteSuccess)
                    loadComments(isRefresh = true)
                }
                .onFailure { exception ->
                    _eventFlow.emit(
                        MyPageCommentsEvent.Comment.DeleteFailed(
                            displayType = ErrorDisplayType.Common,
                            userMessage = "삭제 처리에 실패했습니다.",
                            exceptionMessage = exception.message
                        )
                    )
                    loadComments(isRefresh = true)
                }
        }
    }

    private suspend fun handleFetchError(exception: Throwable) {
        _eventFlow.emit(
            MyPageCommentsEvent.DataFetch.Error(
                displayType = ErrorDisplayType.Common,
                userMessage = "댓글 목록을 불러오지 못했습니다.",
                exceptionMessage = exception.message
            )
        )

        if (_dataState.value is MyPageCommentsDataState.Loading) {
            _dataState.value = MyPageCommentsDataState.Init
        }
    }
}
