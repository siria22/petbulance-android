package com.petbulance.presentation.screen.feature.mypage.sections.help.terms.detail

import androidx.lifecycle.SavedStateHandle
import com.petbulance.domain.model.feature.user.terms.Term
import com.petbulance.domain.model.feature.user.terms.TermsStatus
import com.petbulance.domain.model.type.TermsType
import com.petbulance.domain.usecase.feature.user.terms.GetTermDetailUseCase
import com.petbulance.domain.usecase.feature.user.terms.GetTermsStatusUseCase
import com.petbulance.domain.usecase.feature.user.terms.SaveTermsConsentUseCase
import com.petbulance.domain.usecase.feature.user.terms.WithdrawTermsConsentUseCase
import com.petbulance.presentation.utils.BaseViewModel
import com.petbulance.presentation.utils.error.ErrorDisplayType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TermsDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getTermDetailUseCase: GetTermDetailUseCase,
    private val getTermsStatusUseCase: GetTermsStatusUseCase,
    private val saveTermsConsentUseCase: SaveTermsConsentUseCase,
    private val withdrawTermsConsentUseCase: WithdrawTermsConsentUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<TermsDetailDataState>(TermsDetailDataState.Init)
    val dataState: StateFlow<TermsDetailDataState> = _dataState

    private val _screenState = MutableStateFlow<TermsDetailScreenState>(TermsDetailScreenState.Init)
    val screenState: StateFlow<TermsDetailScreenState> = _screenState

    private val _eventFlow = MutableSharedFlow<TermsDetailEvent>()
    val eventFlow: SharedFlow<TermsDetailEvent> = _eventFlow

    private val _term = MutableStateFlow<Term?>(null)
    val term: StateFlow<Term?> = _term

    private val _termsStatus = MutableStateFlow<TermsStatus?>(null)
    val termsStatus: StateFlow<TermsStatus?> = _termsStatus

    private val _isAgreed = MutableStateFlow(false)
    val isAgreed: StateFlow<Boolean> = _isAgreed

    fun onIntent(intent: TermsDetailIntent) {
        when (intent) {
            is TermsDetailIntent.OnRefresh -> {
                fetchTermDetail()
            }

            is TermsDetailIntent.OnToggleChanged -> {
                handleToggleChange(intent.isAgreed)
            }

            is TermsDetailIntent.DismissRequiredTermsDialog -> {
                _screenState.value = TermsDetailScreenState.Init
            }

            is TermsDetailIntent.DismissContentLoadFailedDialog -> {
                _screenState.value = TermsDetailScreenState.Init
            }
        }
    }

    init {
        observeErrorEvent(eventFlow)
        fetchTermsStatus()
        fetchTermDetail()
    }

    private fun fetchTermsStatus() {
        launch {
            getTermsStatusUseCase()
                .onSuccess { status ->
                    _termsStatus.value = status
                    updateAgreedState()
                }
                .onFailure {
                    // 상태 조회 실패 시 기본값 false
                    _isAgreed.value = false
                }
        }
    }

    private fun updateAgreedState() {
        val term = _term.value
        val status = _termsStatus.value
        if (term == null || status == null) {
            _isAgreed.value = false
            return
        }

        _isAgreed.value = when (term.termsType) {
            TermsType.SERVICE -> status.service
            TermsType.PRIVACY -> status.privacy
            TermsType.LOCATION -> status.location
            TermsType.MARKETING -> status.marketing
            else -> false
        }
    }

    private fun fetchTermDetail() {
        launch {
            val termsType = savedStateHandle.get<String>("termsType") ?: return@launch

            _dataState.value = TermsDetailDataState.Loading

            getTermDetailUseCase(termsType)
                .onSuccess { term ->
                    _term.value = term
                    updateAgreedState()
                    _dataState.value = TermsDetailDataState.Loaded
                    
                    // content가 비어있으면 로드 실패 다이얼로그 표시
                    if (term.content.isBlank()) {
                        _screenState.value = TermsDetailScreenState.ShowContentLoadFailedDialog
                    }
                }
                .onFailure { exception ->
                    _eventFlow.emit(
                        TermsDetailEvent.DataFetch.Error(
                            displayType = ErrorDisplayType.Common,
                            userMessage = "약관 상세 정보를 불러오는데 실패했습니다.",
                            exceptionMessage = exception.message
                        )
                    )
                    _dataState.value = TermsDetailDataState.Init
                    _screenState.value = TermsDetailScreenState.ShowContentLoadFailedDialog
                }
        }
    }

    private fun handleToggleChange(isAgreed: Boolean) {
        val currentTerm = _term.value ?: return

        if (isAgreed) {
            // 약관 동의 처리
            agreeTermsConsent(currentTerm)
        } else if (currentTerm.required) {
            // 필수 약관 철회 시도 -> 다이얼로그 표시
            _screenState.value = TermsDetailScreenState.ShowRequiredTermsDialog(currentTerm)
        } else {
            // 선택 약관 철회
            withdrawTermsConsent(currentTerm)
        }
    }

    private fun agreeTermsConsent(term: Term) {
        launch {
            _dataState.value = TermsDetailDataState.Loading

            saveTermsConsentUseCase(listOf(term.id))
                .onSuccess {
                    // 성공 시 동의 상태 갱신
                    fetchTermsStatus()
                    _dataState.value = TermsDetailDataState.Loaded
                }
                .onFailure { exception ->
                    _eventFlow.emit(
                        TermsDetailEvent.DataFetch.Error(
                            displayType = ErrorDisplayType.Common,
                            userMessage = "약관 동의에 실패했습니다.",
                            exceptionMessage = exception.message
                        )
                    )
                    _dataState.value = TermsDetailDataState.Loaded
                    // 실패 시 Toggle 상태 원복을 위해 상태 갱신
                    updateAgreedState()
                }
        }
    }

    private fun withdrawTermsConsent(term: Term) {
        launch {
            val termsType = term.termsType?.name ?: return@launch

            withdrawTermsConsentUseCase(termsType)
                .onSuccess {
                    // 성공 시 동의 상태 및 약관 정보 갱신
                    fetchTermsStatus()
                    fetchTermDetail()
                }
                .onFailure { exception ->
                    _eventFlow.emit(
                        TermsDetailEvent.TermsWithdraw.Error(
                            displayType = ErrorDisplayType.Common,
                            userMessage = "약관 철회에 실패했습니다.",
                            exceptionMessage = exception.message
                        )
                    )
                }
        }
    }
}
