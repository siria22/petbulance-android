package com.petbulance.presentation.screen.feature.mypage.sections.help.terms

import com.petbulance.domain.model.feature.user.terms.Term
import com.petbulance.domain.usecase.feature.user.terms.GetTermsListUseCase
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
class TermsListViewModel @Inject constructor(
    private val getTermsListUseCase: GetTermsListUseCase,
    private val withdrawTermsConsentUseCase: WithdrawTermsConsentUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<TermsListDataState>(TermsListDataState.Init)
    val dataState: StateFlow<TermsListDataState> = _dataState

    private val _screenState = MutableStateFlow<TermsListScreenState>(TermsListScreenState.Init)
    val screenState: StateFlow<TermsListScreenState> = _screenState

    private val _eventFlow = MutableSharedFlow<TermsListEvent>()
    val eventFlow: SharedFlow<TermsListEvent> = _eventFlow

    private val _termsList = MutableStateFlow<List<Term>>(emptyList())
    val termsList: StateFlow<List<Term>> = _termsList

    fun onIntent(intent: TermsListIntent) {
        when (intent) {
            is TermsListIntent.OnRefresh -> {
                fetchTermsList()
            }

            is TermsListIntent.OnToggleChanged -> {
                handleToggleChange(intent.term, intent.isAgreed)
            }

            is TermsListIntent.DismissRequiredTermsDialog -> {
                _screenState.value = TermsListScreenState.Init
            }
        }
    }

    init {
        observeErrorEvent(eventFlow)
        fetchTermsList()
    }

    private fun fetchTermsList() {
        launch {
            _dataState.value = TermsListDataState.Loading

            getTermsListUseCase()
                .onSuccess { terms ->
                    _termsList.value = terms
                    _dataState.value = TermsListDataState.Loaded
                }
                .onFailure { exception ->
                    _eventFlow.emit(
                        TermsListEvent.DataFetch.Error(
                            displayType = ErrorDisplayType.Common,
                            userMessage = "약관 목록을 불러오는데 실패했습니다.",
                            exceptionMessage = exception.message
                        )
                    )
                    _dataState.value = TermsListDataState.Init
                }
        }
    }

    private fun handleToggleChange(term: Term, isAgreed: Boolean) {
        if (!isAgreed && term.required) {
            // 필수 약관 철회 시도 -> 다이얼로그 표시
            _screenState.value = TermsListScreenState.ShowRequiredTermsDialog(term)
        } else if (!isAgreed) {
            // 선택 약관 철회
            withdrawTermsConsent(term)
        }
        // isAgreed == true인 경우는 동의 처리이므로 별도 처리 불필요 (TODO: 추후 동의 API 연동 시 구현)
    }

    private fun withdrawTermsConsent(term: Term) {
        launch {
            val termsType = term.termsType?.name ?: return@launch

            withdrawTermsConsentUseCase(termsType)
                .onSuccess {
                    // 성공 시 리스트 갱신
                    fetchTermsList()
                }
                .onFailure { exception ->
                    _eventFlow.emit(
                        TermsListEvent.TermsWithdraw.Error(
                            displayType = ErrorDisplayType.Common,
                            userMessage = "약관 철회에 실패했습니다.",
                            exceptionMessage = exception.message
                        )
                    )
                }
        }
    }
}
