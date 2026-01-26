package com.petbulance.presentation.screen.nonfeature.login.terms

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.petbulance.domain.model.feature.user.terms.Term
import com.petbulance.domain.usecase.feature.user.nickname.GetUserTempNickNameUseCase
import com.petbulance.domain.usecase.feature.user.terms.AgreeTermsUseCase
import com.petbulance.domain.usecase.feature.user.terms.GetTermDetailUseCase
import com.petbulance.domain.usecase.feature.user.terms.GetTermsListUseCase
import com.petbulance.domain.utils.LOGGER_TAG
import com.petbulance.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TermsViewModel @Inject constructor(
    private val getTermsListUseCase: GetTermsListUseCase,
    private val agreeTermsUseCase: AgreeTermsUseCase,
    private val getTermDetailUseCase: GetTermDetailUseCase,
    private val getUserTempNickNameUseCase: GetUserTempNickNameUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<TermsDataState>(TermsDataState.Init)
    val dataState: StateFlow<TermsDataState> = _dataState.asStateFlow()

    private val _screenState = MutableStateFlow<TermsScreenState>(TermsScreenState.Init)
    val screenState: StateFlow<TermsScreenState> = _screenState.asStateFlow()

    private val _event = MutableSharedFlow<TermsEvent>()
    val event: SharedFlow<TermsEvent> = _event.asSharedFlow()

    private val _termsList = MutableStateFlow<List<Term>>(emptyList())
    val termsList = _termsList.asStateFlow()

    private val _currentTerm = MutableStateFlow<Term?>(null)
    val currentTerm = _currentTerm.asStateFlow()

    private val _agreedTermIds = MutableStateFlow<Set<Long>>(emptySet())
    val agreedTermIds = _agreedTermIds.asStateFlow()

    private val _userTempName = MutableStateFlow("")
    val userTempName = _userTempName.asStateFlow()

    val isAllRequiredAgreed: StateFlow<Boolean> =
        combine(_termsList, _agreedTermIds) { terms, agreed ->
            if (terms.isEmpty()) false
            else terms.filter { it.required }.all { agreed.contains(it.id) }
        }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    init {
        observeErrorEvent(_event)
        loadTerms()
        loadUserTempNickname()
    }

    fun onIntent(intent: TermsIntent) {
        when (intent) {
            is TermsIntent.OnAgreeClick -> agreeTerms()
            is TermsIntent.OnToggleTerm -> toggleTermConsent(intent.term.id)
            is TermsIntent.OnToggleAll -> toggleAllConsent()

            is TermsIntent.OnDetailClick -> loadTermDetail(intent.term.termsType?.name ?: "UNKNOWN")
            is TermsIntent.OnCloseDetail -> closeTermDetail()
        }
    }

    private fun loadTerms() {
        launch {
            _dataState.value = TermsDataState.Loading
            getTermsListUseCase().onSuccess { list ->
                _termsList.value = list
                _dataState.value = TermsDataState.Init
            }.onFailure { e ->
                _dataState.value = TermsDataState.Init
                _event.emit(TermsEvent.DataFetch.Error(exceptionMessage = e.message))
            }
        }
    }

    private fun toggleTermConsent(termId: Long) {
        _agreedTermIds.update { current ->
            if (current.contains(termId)) current - termId else current + termId
        }
    }

    private fun toggleAllConsent() {
        val allIds = _termsList.value.map { it.id }.toSet()
        if (_agreedTermIds.value.containsAll(allIds)) {
            _agreedTermIds.value = emptySet()
        } else {
            _agreedTermIds.value = allIds
        }
    }

    private fun agreeTerms() {
        launch {
            _dataState.value = TermsDataState.Loading
            val agreedTerms = _termsList.value.filter { _agreedTermIds.value.contains(it.id) }
            agreeTermsUseCase(agreedTerms)
                .onSuccess {
                    _dataState.value = TermsDataState.Init
                    _event.emit(TermsEvent.NavigateToNext)
                }
                .onFailure { e ->
                    _dataState.value = TermsDataState.Init
                    _event.emit(TermsEvent.DataFetch.Error(exceptionMessage = e.message))
                }
        }
    }

    fun loadTermDetail(type: String) {
        launch {
            _dataState.value = TermsDataState.Loading
            getTermDetailUseCase(type).onSuccess { term ->
                _currentTerm.value = term
                _dataState.value = TermsDataState.Init
            }.onFailure { e ->
                _dataState.value = TermsDataState.Init
                _event.emit(TermsEvent.DataFetch.Error(exceptionMessage = e.message))
            }
        }
    }

    private fun closeTermDetail() {
        _currentTerm.value = null
    }

    private fun loadUserTempNickname() {
        launch {
            getUserTempNickNameUseCase()
                .onSuccess { nickname ->
                    _userTempName.value = nickname
                }
                .onFailure { e ->
                    Log.d(
                        LOGGER_TAG,
                        "Failed to load user temp nickname. Use Default value instead:\n" +
                                "${e.message}"
                    )
                    _userTempName.value = "따뜻한햄스터07"
                }
        }
    }
}