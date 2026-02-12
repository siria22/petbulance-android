package com.petbulance.presentation.screen.feature.mypage.sections.activity.reviews

import androidx.lifecycle.SavedStateHandle
import com.petbulance.domain.model.feature.hospital.review.MyReview
import com.petbulance.domain.usecase.feature.hospital.review.DeleteMyReviewsUseCase
import com.petbulance.domain.usecase.feature.hospital.review.GetMyReviewsUseCase
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
class MyPageReviewsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMyReviewsUseCase: GetMyReviewsUseCase,
    private val deleteMyReviewsUseCase: DeleteMyReviewsUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<MyPageReviewsDataState>(MyPageReviewsDataState.Init)
    val dataState: StateFlow<MyPageReviewsDataState> = _dataState.asStateFlow()

    private val _screenState =
        MutableStateFlow<MyPageReviewsScreenState>(MyPageReviewsScreenState.Normal())
    val screenState: StateFlow<MyPageReviewsScreenState> = _screenState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<MyPageReviewsEvent>()
    val eventFlow: SharedFlow<MyPageReviewsEvent> = _eventFlow

    private val currentReviews = mutableListOf<MyReview>()

    init {
        observeErrorEvent(eventFlow)
        onIntent(MyPageReviewsIntent.LoadData)
    }

    fun onIntent(intent: MyPageReviewsIntent) {
        when (intent) {
            is MyPageReviewsIntent.LoadData -> loadReviews(isRefresh = false)
            is MyPageReviewsIntent.Refresh -> loadReviews(isRefresh = true)
            is MyPageReviewsIntent.LoadMore -> loadMoreReviews()
            is MyPageReviewsIntent.ToggleSelectionMode -> toggleSelectionMode(intent.enabled)
            is MyPageReviewsIntent.ToggleReviewSelection -> toggleReviewSelection(intent.reviewId)
            is MyPageReviewsIntent.SelectAll -> selectAll()
            is MyPageReviewsIntent.DeleteSelected -> deleteSelectedReviews()
        }
    }

    private fun loadReviews(isRefresh: Boolean) {
        launch {
            if (!isRefresh) _dataState.value = MyPageReviewsDataState.Loading

            if (isRefresh) {
                _screenState.update { MyPageReviewsScreenState.Normal() }
                currentReviews.clear()
            }

            getMyReviewsUseCase(size = 10, cursorId = null)
                .onSuccess { result ->
                    currentReviews.addAll(result.items)
                    _dataState.value = MyPageReviewsDataState.Loaded(
                        reviews = currentReviews.toList(),
                        nextCursorId = result.nextCursorId,
                        hasNext = result.hasNext
                    )
                }
                .onFailure { handleFetchError(it) }
        }
    }

    private fun loadMoreReviews() {
        val currentState = _dataState.value
        if (currentState !is MyPageReviewsDataState.Loaded || !currentState.hasNext) return

        launch {
            getMyReviewsUseCase(size = 10, cursorId = currentState.nextCursorId)
                .onSuccess { result ->
                    currentReviews.addAll(result.items)
                    _dataState.value = currentState.copy(
                        reviews = currentReviews.toList(),
                        nextCursorId = result.nextCursorId,
                        hasNext = result.hasNext
                    )
                }
                .onFailure { handleFetchError(it) }
        }
    }

    private fun toggleSelectionMode(enabled: Boolean) {
        _screenState.update {
            MyPageReviewsScreenState.Normal(isSelectionMode = enabled, selectedIds = emptySet())
        }
    }

    private fun toggleReviewSelection(reviewId: Long) {
        _screenState.update { state ->
            if (state is MyPageReviewsScreenState.Normal) {
                val newSelection = state.selectedIds.toMutableSet()
                if (newSelection.contains(reviewId)) newSelection.remove(reviewId)
                else newSelection.add(reviewId)
                state.copy(selectedIds = newSelection)
            } else state
        }
    }

    private fun selectAll() {
        _screenState.update { state ->
            if (state is MyPageReviewsScreenState.Normal) {
                val allIds = currentReviews.map { it.id }.toSet()
                // 이미 전부 선택된 상태면 해제, 아니면 전체 선택
                if (state.selectedIds.size == allIds.size && allIds.isNotEmpty()) {
                    state.copy(selectedIds = emptySet())
                } else {
                    state.copy(selectedIds = allIds)
                }
            } else state
        }
    }

    private fun deleteSelectedReviews() {
        val state = _screenState.value as? MyPageReviewsScreenState.Normal ?: return
        val targetIds = state.selectedIds.toList()

        if (targetIds.isEmpty()) return

        launch {
            _dataState.value = MyPageReviewsDataState.Loading

            deleteMyReviewsUseCase(targetIds)
                .onSuccess {
                    _eventFlow.emit(MyPageReviewsEvent.Review.DeleteSuccess)
                    loadReviews(isRefresh = true)
                }
                .onFailure { exception ->
                    _eventFlow.emit(
                        MyPageReviewsEvent.Review.DeleteFailed(
                            displayType = ErrorDisplayType.Common,
                            userMessage = "삭제 처리에 실패했습니다.",
                            exceptionMessage = exception.message
                        )
                    )
                    _dataState.value = MyPageReviewsDataState.Loaded(
                        reviews = currentReviews.toList(),
                        hasNext = false
                    )
                }
        }
    }

    private suspend fun handleFetchError(exception: Throwable) {
        _eventFlow.emit(
            MyPageReviewsEvent.DataFetch.Error(
                displayType = ErrorDisplayType.Common,
                userMessage = "리뷰 목록을 불러오지 못했습니다.",
                exceptionMessage = exception.message
            )
        )

        if (_dataState.value is MyPageReviewsDataState.Loading) {
            _dataState.value = MyPageReviewsDataState.Init
        }
    }
}