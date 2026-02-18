package com.petbulance.presentation.screen.feature.mypage.sections.help.notice.detail

import androidx.lifecycle.SavedStateHandle
import com.petbulance.domain.model.feature.support.notice.NoticeDetail
import com.petbulance.domain.usecase.feature.support.notice.GetNoticeDetailUseCase
import com.petbulance.domain.exception.NotFoundException
import com.petbulance.presentation.utils.BaseViewModel
import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.nav.ScreenDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyPageNoticeDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getNoticeDetailUseCase: GetNoticeDetailUseCase
) : BaseViewModel() {

    private val _dataState =
        MutableStateFlow<MyPageNoticeDetailDataState>(MyPageNoticeDetailDataState.Init)
    val dataState: StateFlow<MyPageNoticeDetailDataState> = _dataState.asStateFlow()

    private val _screenState =
        MutableStateFlow<MyPageNoticeDetailScreenState>(MyPageNoticeDetailScreenState.Init)
    val screenState: StateFlow<MyPageNoticeDetailScreenState> = _screenState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<MyPageNoticeDetailEvent>()
    val eventFlow: SharedFlow<MyPageNoticeDetailEvent> = _eventFlow

    private val _noticeDetail = MutableStateFlow<NoticeDetail?>(null)
    val noticeDetail: StateFlow<NoticeDetail?> = _noticeDetail.asStateFlow()

    init {
        observeErrorEvent(_eventFlow)
        val noticeId = savedStateHandle.get<Long>(ScreenDestinations.MyPage.Help.Notice.Detail.ARG_ID)
        if (noticeId != null) {
            onIntent(MyPageNoticeDetailIntent.LoadDetail(noticeId))
        }
    }

    fun onIntent(intent: MyPageNoticeDetailIntent) {
        when (intent) {
            is MyPageNoticeDetailIntent.LoadDetail -> loadDetail(intent.noticeId)
        }
    }

    private fun loadDetail(noticeId: Long) {
        launch {
            _dataState.value = MyPageNoticeDetailDataState.Loading

            getNoticeDetailUseCase(noticeId)
                .onSuccess { detail ->
                    _noticeDetail.value = detail
                    _dataState.value = MyPageNoticeDetailDataState.Init
                }
                .onFailure { exception ->
                    if (exception is NotFoundException) {
                        _dataState.value = MyPageNoticeDetailDataState.NotFound
                    } else {
                        _eventFlow.emit(
                            MyPageNoticeDetailEvent.DataFetch.Error(
                                displayType = ErrorDisplayType.Common,
                                userMessage = "공지사항을 불러오는데 실패했습니다.",
                                exceptionMessage = exception.message
                            )
                        )
                        _dataState.value = MyPageNoticeDetailDataState.Init
                    }
                }
        }
    }
}
