package com.petbulance.presentation.screen.feature.mypage.sections.user.profile

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
class MyPageProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    // private val someUseCase: SomeUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<MyPageProfileDataState>(MyPageProfileDataState.Init)
    val dataState: StateFlow<MyPageProfileDataState> = _dataState

    private val _screenState = MutableStateFlow<MyPageProfileScreenState>(MyPageProfileScreenState.Init)
    val screenState: StateFlow<MyPageProfileScreenState> = _screenState

    private val _eventFlow = MutableSharedFlow<MyPageProfileEvent>()
    val eventFlow: SharedFlow<MyPageProfileEvent> = _eventFlow

    private val _someData = MutableStateFlow("")
    val someData: StateFlow<String> = _someData

    fun onIntent(intent: MyPageProfileIntent) {
        when (intent) {
            is MyPageProfileIntent.SomeIntentWithoutParamsMyPage -> {
                //do sth
            }

            is MyPageProfileIntent.SomeIntentWithParamsMyPage -> {
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
        _dataState.value = MyPageProfileDataState.OnProgress

        runCatching {
            // someUseCase()
        }.onSuccess { result ->
            // some.value = result.getOrThrow()
        }.onFailure { exception ->
            // some.value = emptyList()
            _eventFlow.emit(
                MyPageProfileEvent.DataFetch.Error(
                    displayType = ErrorDisplayType.Common, // will be handled by BaseViewModel & CommonScreenWrapper
                    userMessage = "Error messages to be shown to users",
                    exceptionMessage = exception.message
                )
            )
        }
        _dataState.value = MyPageProfileDataState.Init
    }
}
