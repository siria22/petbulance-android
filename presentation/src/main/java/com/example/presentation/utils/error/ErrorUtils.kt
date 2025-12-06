package com.example.presentation.utils.error

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.filter

sealed interface ErrorDisplayType {
    data object Common : ErrorDisplayType
    data object Custom : ErrorDisplayType
}

interface ErrorEvent {
    val userMessage: String
    val exceptionMessage: String?
    val displayType: ErrorDisplayType
}

/**
 * SharedFlow에서 ErrorEvent 중 Custom 타입인 것만 필터링하여 수집합니다.
 * @param action 수집된 Custom 에러 이벤트를 처리할 동작
 */
suspend fun <T> SharedFlow<T>.collectCustomErrors(
    action: suspend (error: ErrorEvent) -> Unit
) {
    this.filter { it is ErrorEvent && it.displayType == ErrorDisplayType.Custom }
        .collect {
            action(it as ErrorEvent)
        }
}