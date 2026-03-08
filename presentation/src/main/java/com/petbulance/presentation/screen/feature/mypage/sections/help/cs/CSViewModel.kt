package com.petbulance.presentation.screen.feature.mypage.sections.help.cs

import androidx.lifecycle.SavedStateHandle
import com.petbulance.presentation.utils.BaseViewModel
import com.petbulance.presentation.utils.error.ErrorDisplayType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CSViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    // private val someUseCase: SomeUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<CSDataState>(CSDataState.Init)
    val dataState: StateFlow<CSDataState> = _dataState

    private val _screenState = MutableStateFlow<CSScreenState>(CSScreenState.Init)
    val screenState: StateFlow<CSScreenState> = _screenState

    private val _eventFlow = MutableSharedFlow<CSEvent>()
    val eventFlow: SharedFlow<CSEvent> = _eventFlow

    fun onIntent(intent: CSIntent) {
        when (intent) {
            is CSIntent.SomeIntentWithoutParams -> {
                //do sth
            }

            is CSIntent.SomeIntentWithParams -> {
                //do sth(intent.params)
            }
        }
    }

    init {
        observeErrorEvent(eventFlow)

        launch {

        }
    }

    // some function
    private suspend fun someFunction() {
        _dataState.value = CSDataState.OnProgress

        runCatching {
            // someUseCase()
        }.onSuccess { result ->
            // some.value = result.getOrThrow()
        }.onFailure { exception ->
            // some.value = emptyList()
            _eventFlow.emit(
                CSEvent.DataFetch.Error(
                    displayType = ErrorDisplayType.Common, // will be handled by BaseViewModel & CommonScreenWrapper
                    userMessage = "Error messages to be shown to users",
                    exceptionMessage = exception.message
                )
            )
        }
        _dataState.value = CSDataState.Init
    }
}
