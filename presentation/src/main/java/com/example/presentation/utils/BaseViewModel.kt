package com.example.presentation.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.presentation.utils.error.ErrorDialogState
import com.example.presentation.utils.error.ErrorDisplayType
import com.example.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _errorDialogState = MutableStateFlow(ErrorDialogState.idle())
    val errorDialogState = _errorDialogState.asStateFlow()

    fun dismissErrorDialog() {
        _errorDialogState.value = ErrorDialogState.idle()
    }

    protected fun launch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch { block() }
    }

    protected fun <T> observeErrorEvent(
        eventFlow: SharedFlow<T>
    ) {
        launch {
            eventFlow
                .filterIsInstance<ErrorEvent>()
                .collect { event ->
                    if (event.displayType == ErrorDisplayType.Common) {
                        _errorDialogState.value = ErrorDialogState.setErrorEvent(event)
                    }
                }
        }
    }
}