package com.petbulance.presentation.screen.feature.community

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
class CommunityViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    // private val someUseCase: SomeUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<CommunityDataState>(CommunityDataState.Init)
    val dataState: StateFlow<CommunityDataState> = _dataState

    private val _screenState = MutableStateFlow<CommunityScreenState>(CommunityScreenState.Init)
    val screenState: StateFlow<CommunityScreenState> = _screenState

    private val _eventFlow = MutableSharedFlow<CommunityEvent>()
    val eventFlow: SharedFlow<CommunityEvent> = _eventFlow

    private val _someData = MutableStateFlow("")
    val someData: StateFlow<String> = _someData

    fun onIntent(intent: CommunityIntent) {
        when (intent) {
            is CommunityIntent.SomeIntentWithoutParams -> {
                //do sth
            }

            is CommunityIntent.SomeIntentWithParams -> {
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
        _dataState.value = CommunityDataState.OnProgress

        runCatching {
            // someUseCase()
        }.onSuccess { result ->
            // some.value = result.getOrThrow()
        }.onFailure { exception ->
            // some.value = emptyList()
            _eventFlow.emit(
                CommunityEvent.DataFetch.Error(
                    displayType = ErrorDisplayType.Common, // will be handled by BaseViewModel & CommonScreenWrapper
                    userMessage = "Error messages to be shown to users",
                    exceptionMessage = exception.message
                )
            )
        }
        _dataState.value = CommunityDataState.Init
    }
}
