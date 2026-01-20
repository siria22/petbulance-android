package com.example.presentation.screen.feature.review.camera

import com.example.presentation.screen.feature.review.create.ReceiptAnalysisResultUiModel

sealed interface ReceiptCameraEvent {
    data class AnalysisSuccess(val result: ReceiptAnalysisResultUiModel) : ReceiptCameraEvent
    data class ShowError(val message: String) : ReceiptCameraEvent
    data object AnalysisFailed : ReceiptCameraEvent
}

data class ReceiptCameraState(
    val isAnalyzing: Boolean = false
)