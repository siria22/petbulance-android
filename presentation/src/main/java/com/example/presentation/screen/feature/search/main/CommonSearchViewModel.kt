package com.example.presentation.screen.feature.search.main

import com.example.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CommonSearchViewModel @Inject constructor() : BaseViewModel() {

    private val _screenState =
        MutableStateFlow<SearchScreenState>(SearchScreenState.Hospitals.MapView)
    val screenState: StateFlow<SearchScreenState> = _screenState

    private val _eventFlow = MutableSharedFlow<SearchEvent>()
    val eventFlow: SharedFlow<SearchEvent> = _eventFlow

    fun onIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.ChangeScreenState -> {
                _screenState.value = intent.state
            }
        }
    }

    init {
        observeErrorEvent(eventFlow)

        launch {

        }
    }

}
