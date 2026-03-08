package com.petbulance.presentation.screen.feature.mypage.sections.help.cs

import com.petbulance.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CSViewModel @Inject constructor() : BaseViewModel() {

    private val _dataState = MutableStateFlow<CSDataState>(CSDataState.Init)
    val dataState: StateFlow<CSDataState> = _dataState

    private val _screenState = MutableStateFlow<CSScreenState>(CSScreenState.Init)
    val screenState: StateFlow<CSScreenState> = _screenState

    private val _eventFlow = MutableSharedFlow<CSEvent>()
    val eventFlow: SharedFlow<CSEvent> = _eventFlow

    init {
        observeErrorEvent(eventFlow)
    }
}
