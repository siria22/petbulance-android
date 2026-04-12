package com.petbulance.presentation.screen.feature.mypage.sections.help.cs.qna.create

import androidx.lifecycle.SavedStateHandle
import com.petbulance.domain.model.feature.support.qna.QnaParam
import com.petbulance.domain.usecase.feature.support.qna.CreateQnaUseCase
import com.petbulance.domain.usecase.feature.support.qna.GetQnaByIdUseCase
import com.petbulance.domain.usecase.feature.support.qna.UpdateQnaUseCase
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
class QnaCreateViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getQnaByIdUseCase: GetQnaByIdUseCase,
    private val createQnaUseCase: CreateQnaUseCase,
    private val updateQnaUseCase: UpdateQnaUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<QnaCreateDataState>(QnaCreateDataState.Init)
    val dataState: StateFlow<QnaCreateDataState> = _dataState

    private val _screenState = MutableStateFlow<QnaCreateScreenState>(QnaCreateScreenState.Init)
    val screenState: StateFlow<QnaCreateScreenState> = _screenState

    private val _eventFlow = MutableSharedFlow<QnaCreateEvent>()
    val eventFlow: SharedFlow<QnaCreateEvent> = _eventFlow

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private val _content = MutableStateFlow("")
    val content: StateFlow<String> = _content

    private val _isSubmitEnabled = MutableStateFlow(false)
    val isSubmitEnabled: StateFlow<Boolean> = _isSubmitEnabled

    private val _mode = MutableStateFlow(QnaCreateMode.CREATE)
    val mode: StateFlow<QnaCreateMode> = _mode

    private val _qnaId = MutableStateFlow<Long?>(null)
    val qnaId: StateFlow<Long?> = _qnaId

    fun onIntent(intent: QnaCreateIntent) {
        when (intent) {
            is QnaCreateIntent.OnTitleChanged -> {
                _title.value = intent.title.take(100) // 최대 100자 제한
                updateSubmitButtonState()
            }

            is QnaCreateIntent.OnContentChanged -> {
                _content.value = intent.content.take(1000) // 최대 1000자 제한
                updateSubmitButtonState()
            }

            is QnaCreateIntent.OnSubmitClicked -> {
                if (_isSubmitEnabled.value) {
                    submitQna()
                }
            }
        }
    }

    private fun updateSubmitButtonState() {
        _isSubmitEnabled.value = _title.value.isNotBlank() && _content.value.isNotBlank()
    }

    init {
        observeErrorEvent(eventFlow)

        launch {
            val qnaId = savedStateHandle.get<String>(ScreenDestinations.MyPage.Help.CS.Qna.Create.ARG_QNA_ID)?.toLongOrNull()
            if (qnaId != null && qnaId > 0) {
                _mode.value = QnaCreateMode.EDIT
                _qnaId.value = qnaId
                fetchQnaDetail(qnaId)
            }
        }
    }

    private fun fetchQnaDetail(qnaId: Long) {
        launch {
            _dataState.value = QnaCreateDataState.OnProgress

            getQnaByIdUseCase(qnaId)
                .onSuccess { qna ->
                    _title.value = qna.title
                    _content.value = qna.content
                    updateSubmitButtonState()
                }.onFailure { exception ->
                    _eventFlow.emit(
                        QnaCreateEvent.DataFetch.Error(
                            displayType = ErrorDisplayType.Common,
                            userMessage = "문의 내역을 불러오는데 실패했습니다.",
                            exceptionMessage = exception.message
                        )
                    )
                }

            _dataState.value = QnaCreateDataState.Init
        }
    }

    private fun submitQna() {
        launch {
            _dataState.value = QnaCreateDataState.OnProgress

            val param = QnaParam(
                title = _title.value.trim(),
                content = _content.value.trim()
            )

            val qnaId = _qnaId.value
            val result = if (_mode.value == QnaCreateMode.EDIT && qnaId != null) {
                updateQnaUseCase(qnaId, param)
            } else {
                createQnaUseCase(param)
            }

            result.onSuccess { qna ->
                _eventFlow.emit(QnaCreateEvent.SubmitSuccess(qna.id))
            }.onFailure { exception ->
                _eventFlow.emit(
                    QnaCreateEvent.Submit.Error(
                        displayType = ErrorDisplayType.Common,
                        userMessage = if (_mode.value == QnaCreateMode.EDIT) "문의 수정에 실패했습니다." else "문의 등록에 실패했습니다.",
                        exceptionMessage = exception.message
                    )
                )
            }

            _dataState.value = QnaCreateDataState.Init
        }
    }
}
