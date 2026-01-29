package com.petbulance.presentation.screen.feature.mypage.profile

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
class ProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    // private val someUseCase: SomeUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<ProfileDataState>(ProfileDataState.Init)
    val dataState: StateFlow<ProfileDataState> = _dataState

    private val _screenState = MutableStateFlow<ProfileScreenState>(ProfileScreenState.Init)
    val screenState: StateFlow<ProfileScreenState> = _screenState

    private val _eventFlow = MutableSharedFlow<ProfileEvent>()
    val eventFlow: SharedFlow<ProfileEvent> = _eventFlow

    private val _someData = MutableStateFlow("")
    val someData: StateFlow<String> = _someData

    fun onIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.SomeIntentWithoutParams -> {
                //do sth
            }

            is ProfileIntent.SomeIntentWithParams -> {
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
        _dataState.value = ProfileDataState.OnProgress

        runCatching {
            // someUseCase()
        }.onSuccess { result ->
            // some.value = result.getOrThrow()
        }.onFailure { exception ->
            // some.value = emptyList()
            _eventFlow.emit(
                ProfileEvent.DataFetch.Error(
                    displayType = ErrorDisplayType.Common, // will be handled by BaseViewModel & CommonScreenWrapper
                    userMessage = "Error messages to be shown to users",
                    exceptionMessage = exception.message
                )
            )
        }
        _dataState.value = ProfileDataState.Init
    }
}
