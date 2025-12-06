package com.example.presentation.screen.home

import androidx.lifecycle.SavedStateHandle
import com.example.presentation.utils.BaseViewModel
import com.example.presentation.utils.error.ErrorDisplayType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    // private val someUseCase: SomeUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<HomeDataState>(HomeDataState.Init)
    val dataState: StateFlow<HomeDataState> = _dataState

    private val _screenState = MutableStateFlow<HomeScreenState>(HomeScreenState.Init)
    val screenState: StateFlow<HomeScreenState> = _screenState

    private val _eventFlow = MutableSharedFlow<HomeEvent>()
    val eventFlow: SharedFlow<HomeEvent> = _eventFlow

    private val _someData = MutableStateFlow("")
    val someData: StateFlow<String> = _someData

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.SomeIntentWithoutParams -> {
                //do sth
            }

            is HomeIntent.SomeIntentWithParams -> {
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
        _dataState.value = HomeDataState.OnProgress

        runCatching {
            // someUseCase()
        }.onSuccess { result ->
            // some.value = result.getOrThrow()
        }.onFailure { exception ->
            // some.value = emptyList()
            _eventFlow.emit(
                HomeEvent.DataFetch.Error(
                    displayType = ErrorDisplayType.Common, // will be handled by BaseViewModel & CommonScreenWrapper
                    userMessage = "Error messages to be shown to users",
                    exceptionMessage = exception.message
                )
            )
        }
        _dataState.value = HomeDataState.Init
    }
}
