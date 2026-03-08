package com.petbulance.presentation.screen.feature.mypage.sections.help.cs.qna.list

import androidx.lifecycle.SavedStateHandle
import com.petbulance.domain.model.feature.support.qna.Qna
import com.petbulance.domain.usecase.feature.support.qna.GetQnaListUseCase
import com.petbulance.presentation.utils.BaseViewModel
import com.petbulance.presentation.utils.error.ErrorDisplayType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class QnaListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getQnaListUseCase: GetQnaListUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<QnaListDataState>(QnaListDataState.Init)
    val dataState: StateFlow<QnaListDataState> = _dataState

    private val _screenState = MutableStateFlow<QnaListScreenState>(QnaListScreenState.Init)
    val screenState: StateFlow<QnaListScreenState> = _screenState

    private val _eventFlow = MutableSharedFlow<QnaListEvent>()
    val eventFlow: SharedFlow<QnaListEvent> = _eventFlow

    private val _qnaList = MutableStateFlow<List<Qna>>(emptyList())
    val qnaList: StateFlow<List<Qna>> = _qnaList

    private val _successMessage = MutableStateFlow<SuccessMessageType?>(null)
    val successMessage: StateFlow<SuccessMessageType?> = _successMessage

    private var lastQnaId: Long? = null
    private var hasNext: Boolean = true
    private var lastLoadMoreTime: Long = 0L
    private var previousListSize: Int = 0

    fun onIntent(intent: QnaListIntent) {
        when (intent) {
            is QnaListIntent.OnRefresh -> {
                refreshQnaList()
            }

            is QnaListIntent.OnLoadMore -> {
                val currentTime = System.currentTimeMillis()
                if (hasNext && 
                    _dataState.value !is QnaListDataState.Loading &&
                    (currentTime - lastLoadMoreTime) > 500L) { // 500ms debounce
                    lastLoadMoreTime = currentTime
                    loadMoreQnaList()
                }
            }
        }
    }

    init {
        observeErrorEvent(eventFlow)
        
        launch {
            val resultType = savedStateHandle.get<String>("resultType")
            val qnaId = savedStateHandle.get<Long>("qnaId")

            when (resultType) {
                "created" -> qnaId?.let { _successMessage.value = SuccessMessageType.Created(it) }
                "updated" -> qnaId?.let { _successMessage.value = SuccessMessageType.Updated(it) }
                "deleted" -> _successMessage.value = SuccessMessageType.Deleted
            }

            savedStateHandle.remove<String>("resultType")
            savedStateHandle.remove<Long>("qnaId")
        }

        refreshQnaList()
    }

    private fun refreshQnaList() {
        launch {
            previousListSize = _qnaList.value.size
            _dataState.value = QnaListDataState.Loading

            getQnaListUseCase(lastQnaId = null, pageSize = 10)
                .onSuccess { qnaListResult ->
                    _qnaList.value = qnaListResult.qnaList
                    lastQnaId = qnaListResult.qnaList.lastOrNull()?.id
                    hasNext = qnaListResult.hasNext
                    _dataState.value = QnaListDataState.Loaded(hasNext = hasNext)
                }.onFailure { exception ->
                    _eventFlow.emit(
                        QnaListEvent.DataFetch.Error(
                            displayType = ErrorDisplayType.Common,
                            userMessage = "문의 목록을 불러오는데 실패했습니다.",
                            exceptionMessage = exception.message
                        )
                    )
                    _dataState.value = QnaListDataState.Init
                }
        }
    }

    private fun loadMoreQnaList() {
        launch {
            _dataState.value = QnaListDataState.Loading

            getQnaListUseCase(lastQnaId = lastQnaId, pageSize = 10)
                .onSuccess { qnaListResult ->
                    _qnaList.value = _qnaList.value + qnaListResult.qnaList
                    lastQnaId = qnaListResult.qnaList.lastOrNull()?.id
                    hasNext = qnaListResult.hasNext
                    _dataState.value = QnaListDataState.Loaded(hasNext = hasNext)
                }.onFailure { exception ->
                    _eventFlow.emit(
                        QnaListEvent.DataFetch.Error(
                            displayType = ErrorDisplayType.Common,
                            userMessage = "문의 목록을 불러오는데 실패했습니다.",
                            exceptionMessage = exception.message
                        )
                    )
                    _dataState.value = QnaListDataState.Loaded(hasNext = hasNext)
                }
        }
    }

    fun dismissSuccessMessage() {
        _successMessage.value = null
    }
}
