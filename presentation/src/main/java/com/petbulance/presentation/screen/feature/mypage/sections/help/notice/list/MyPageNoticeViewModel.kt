package com.petbulance.presentation.screen.feature.mypage.sections.help.notice.list

import com.petbulance.domain.model.feature.support.notice.NoticeListItem
import com.petbulance.domain.usecase.feature.support.notice.GetNoticeListUseCase
import com.petbulance.presentation.utils.BaseViewModel
import com.petbulance.presentation.utils.error.ErrorDisplayType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageNoticeViewModel @Inject constructor(
    private val getNoticeListUseCase: GetNoticeListUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<MyPageNoticeDataState>(MyPageNoticeDataState.Init)
    val dataState: StateFlow<MyPageNoticeDataState> = _dataState

    private val _screenState =
        MutableStateFlow<MyPageNoticeScreenState>(MyPageNoticeScreenState.Init)
    val screenState: StateFlow<MyPageNoticeScreenState> = _screenState

    private val _eventFlow = MutableSharedFlow<MyPageNoticeEvent>()
    val eventFlow: SharedFlow<MyPageNoticeEvent> = _eventFlow

    private val _notices = MutableStateFlow<List<NoticeListItem>>(emptyList())
    val notices: StateFlow<List<NoticeListItem>> = _notices.asStateFlow()

    private val _isLoadingNextPage = MutableStateFlow(false)
    val isLoadingNextPage: StateFlow<Boolean> = _isLoadingNextPage.asStateFlow()

    private var lastNoticeId: Long? = null
    private var hasNextPage: Boolean = true
    private var isLoading: Boolean = false
    private val pageSize = 10

    init {
        observeErrorEvent(_eventFlow)
        loadNotices(isRefresh = true)
    }

    fun onIntent(intent: MyPageNoticeIntent) {
        when (intent) {
            is MyPageNoticeIntent.LoadMore -> {
                if (!isLoading && hasNextPage) {
                    loadNotices(isRefresh = false)
                }
            }

            is MyPageNoticeIntent.Refresh -> {
                loadNotices(isRefresh = true)
            }
        }
    }

    private fun loadNotices(isRefresh: Boolean) {
        launch {
            if (isRefresh) {
                _dataState.value = MyPageNoticeDataState.Loading
                lastNoticeId = null
                hasNextPage = true
                _notices.value = emptyList()
            } else {
                _isLoadingNextPage.value = true
            }

            isLoading = true

            getNoticeListUseCase(
                lastNoticeId = lastNoticeId,
                pageSize = pageSize
            ).onSuccess { pagingResult ->
                lastNoticeId = pagingResult.content.lastOrNull()?.noticeId
                hasNextPage = pagingResult.hasNext

                _notices.update { currentList ->
                    if (isRefresh) {
                        pagingResult.content
                    } else {
                        currentList + pagingResult.content
                    }
                }

                if (isRefresh) {
                    _dataState.value = MyPageNoticeDataState.Init
                } else {
                    _isLoadingNextPage.value = false
                }
            }.onFailure { exception ->
                launch {
                    _eventFlow.emit(
                        MyPageNoticeEvent.DataFetch.Error(
                            displayType = ErrorDisplayType.Common,
                            userMessage = "공지사항을 불러오는데 실패했습니다.",
                            exceptionMessage = exception.message
                        )
                    )
                }

                if (isRefresh) {
                    _dataState.value = MyPageNoticeDataState.Init
                } else {
                    _isLoadingNextPage.value = false
                }
            }

            isLoading = false
        }
    }
}
