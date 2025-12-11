package com.example.presentation.screen.feature.search

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
class CommonSearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    // private val someUseCase: SomeUseCase
) : BaseViewModel() {

    private val _screenState =
        MutableStateFlow<SearchScreenState>(SearchScreenState.Hospitals.MapView)
    val screenState: StateFlow<SearchScreenState> = _screenState

    private val _eventFlow = MutableSharedFlow<SearchEvent>()
    val eventFlow: SharedFlow<SearchEvent> = _eventFlow

    private val _someData = MutableStateFlow("")
    val someData: StateFlow<String> = _someData

    init {
        observeErrorEvent(eventFlow)

        launch {

        }
    }

}
