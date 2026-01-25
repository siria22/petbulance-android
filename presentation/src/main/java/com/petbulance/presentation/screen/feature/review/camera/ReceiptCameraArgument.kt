package com.petbulance.presentation.screen.feature.review.camera

import android.content.ContentResolver
import android.net.Uri
import androidx.camera.core.ImageProxy
import com.petbulance.presentation.screen.feature.review.create.ReceiptAnalysisResultUiModel
import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class ReceiptCameraArgument(
    val state: ReceiptCameraState,
    val intent: (ReceiptCameraIntent) -> Unit,
    val event: SharedFlow<ReceiptCameraEvent>
)

data class ReceiptCameraState(
    val isAnalyzing: Boolean = false
)

sealed interface ReceiptCameraIntent {
    data class PhotoCaptured(val imageProxy: ImageProxy) : ReceiptCameraIntent
    data class GalleryImageSelected(val uri: Uri, val contentResolver: ContentResolver) : ReceiptCameraIntent
}

sealed interface ReceiptCameraEvent {
    data class AnalysisSuccess(val result: ReceiptAnalysisResultUiModel) : ReceiptCameraEvent
    data class AnalysisFailed(
        override val userMessage: String,
        override val exceptionMessage: String?,
        override val displayType: ErrorDisplayType = ErrorDisplayType.Common
    ) : ReceiptCameraEvent, ErrorEvent
    data class ShowError(val message: String) : ReceiptCameraEvent
}
