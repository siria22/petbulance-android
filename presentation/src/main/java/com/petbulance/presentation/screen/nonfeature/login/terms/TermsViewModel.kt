package com.petbulance.presentation.screen.nonfeature.login.terms

import androidx.lifecycle.viewModelScope
import com.petbulance.domain.model.feature.user.terms.Term
import com.petbulance.domain.usecase.feature.user.terms.AgreeTermsUseCase
import com.petbulance.domain.usecase.feature.user.terms.GetTermDetailUseCase
import com.petbulance.domain.usecase.feature.user.terms.GetTermsListUseCase
import com.petbulance.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class TermsViewModel @Inject constructor(
    private val getTermsListUseCase: GetTermsListUseCase,
    private val agreeTermsUseCase: AgreeTermsUseCase,
    private val getTermDetailUseCase: GetTermDetailUseCase
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

    val isAllRequiredAgreed: StateFlow<Boolean> = combine(_termsList, _agreedTermIds) { terms, agreed ->
        if (terms.isEmpty()) false
        else terms.filter { it.required }.all { agreed.contains(it.id) }
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    init {
        observeErrorEvent(_event)
        loadTerms()
    }

    fun onIntent(intent: TermsIntent) {
        when (intent) {
            is TermsIntent.OnAgreeClick -> agreeTerms()
            is TermsIntent.OnToggleTerm -> toggleTermConsent(intent.termId)
            is TermsIntent.OnToggleAll -> toggleAllConsent()

            is TermsIntent.OnDetailClick -> loadTermDetail(intent.termId.toString())
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
}