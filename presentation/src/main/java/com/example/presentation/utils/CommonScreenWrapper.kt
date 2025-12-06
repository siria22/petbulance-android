package com.example.presentation.utils

import androidx.compose.runtime.Composable
import com.example.presentation.utils.error.ErrorDialog
import com.example.presentation.utils.error.ErrorDialogState

/**
 * 공용 에러 핸들링을 위한 Wrapper
 */
@Composable
fun CommonScreenWrapper(
    errorState: ErrorDialogState,
    dismissErrorDialog: () -> Unit,
    content: @Composable () -> Unit
) {

    content()

    if (errorState.isErrorDialogVisible) {
        ErrorDialog(
            errorDialogState = errorState,
            errorHandler = {
                dismissErrorDialog()
            }
        )
    }
}