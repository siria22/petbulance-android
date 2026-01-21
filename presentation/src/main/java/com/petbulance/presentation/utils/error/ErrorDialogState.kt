package com.petbulance.presentation.utils.error

import android.util.Log
import com.petbulance.domain.utils.LOGGER_TAG

data class ErrorDialogState(
    val userMessage: String,
    val exceptionMessage: String?,
    val isErrorDialogVisible: Boolean,
) {
    fun logErrorEvent() {
        Log.e(
            LOGGER_TAG,
            "userMessage : $userMessage\nexceptionMessage : ${exceptionMessage ?: "Unknown error"}"
        )
    }

    companion object {
        fun idle() =
            ErrorDialogState(userMessage = "", exceptionMessage = "", isErrorDialogVisible = false)

        fun setErrorEvent(
            errorEvent: ErrorEvent
        ): ErrorDialogState =
            ErrorDialogState(
                userMessage = errorEvent.userMessage,
                exceptionMessage = errorEvent.exceptionMessage,
                isErrorDialogVisible = true,
            )
    }
}