package com.petbulance.presentation.screen.feature.mypage.sections.help.cs.qna.detail

import androidx.lifecycle.SavedStateHandle
import com.petbulance.domain.usecase.feature.support.qna.DeleteQnaUseCase
import com.petbulance.domain.usecase.feature.support.qna.GetQnaByIdUseCase
import com.petbulance.presentation.utils.BaseViewModel
import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.nav.ScreenDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class QnaDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getQnaByIdUseCase: GetQnaByIdUseCase,
    private val deleteQnaUseCase: DeleteQnaUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<QnaDetailDataState>(QnaDetailDataState.Init)
    val dataState: StateFlow<QnaDetailDataState> = _dataState

    private val _screenState = MutableStateFlow<QnaDetailScreenState>(QnaDetailScreenState.Init)
    val screenState: StateFlow<QnaDetailScreenState> = _screenState

    private val _eventFlow = MutableSharedFlow<QnaDetailEvent>()
    val eventFlow: SharedFlow<QnaDetailEvent> = _eventFlow

    private val _uiState = MutableStateFlow(QnaDetailUiState.empty)
    val uiState: StateFlow<QnaDetailUiState> = _uiState

    fun onIntent(intent: QnaDetailIntent) {
        when (intent) {
            is QnaDetailIntent.OnDeleteConfirmed -> {
                deleteQna()
            }
        }
    }

    init {
        observeErrorEvent(eventFlow)

        launch {
            val qnaId = savedStateHandle.get<Long>(ScreenDestinations.MyPage.Help.CS.Qna.Detail.ARG_ID)
            if (qnaId != null) {
                fetchQnaDetail(qnaId)
            } else {
                _eventFlow.emit(
                    QnaDetailEvent.DataFetch.Error(
                        displayType = ErrorDisplayType.Common,
                        userMessage = "잘못된 접근입니다.",
                        exceptionMessage = "QnA ID is null"
                    )
                )
            }
        }
    }

    private fun fetchQnaDetail(qnaId: Long) {
        launch {
            _dataState.value = QnaDetailDataState.OnProgress
            _uiState.value = _uiState.value.copy(isLoading = true)

            getQnaByIdUseCase(qnaId)
                .onSuccess { qna ->
                    _uiState.value = QnaDetailUiState(
                        qna = qna,
                        isLoading = false
                    )
                }.onFailure { exception ->
                    _eventFlow.emit(
                        QnaDetailEvent.DataFetch.Error(
                            displayType = ErrorDisplayType.Common,
                            userMessage = "문의 내역을 불러오는데 실패했습니다.",
                            exceptionMessage = exception.message
                        )
                    )
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }

            _dataState.value = QnaDetailDataState.Init
        }
    }

    private fun deleteQna() {
        launch {
            _dataState.value = QnaDetailDataState.OnProgress

            val qnaId = _uiState.value.qna?.id ?: return@launch
            deleteQnaUseCase(qnaId)
                .onSuccess {
                    _eventFlow.emit(QnaDetailEvent.Delete.Success)
                }.onFailure { exception ->
                    _eventFlow.emit(
                        QnaDetailEvent.Delete.Error(
                            displayType = ErrorDisplayType.Common,
                            userMessage = "문의 삭제에 실패했습니다.",
                            exceptionMessage = exception.message
                        )
                    )
                }

            _dataState.value = QnaDetailDataState.Init
        }
    }
}
