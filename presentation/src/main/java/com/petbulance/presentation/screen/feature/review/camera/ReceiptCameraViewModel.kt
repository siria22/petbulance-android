package com.petbulance.presentation.screen.feature.review.camera

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import androidx.camera.core.ImageProxy
import androidx.core.graphics.scale
import androidx.lifecycle.viewModelScope
import com.petbulance.domain.usecase.feature.hospital.review.AnalyzeReceiptUseCase
import com.petbulance.presentation.screen.feature.review.create.ReceiptAnalysisResultUiModel
import com.petbulance.presentation.screen.feature.review.create.ReceiptItemUiModel
import com.petbulance.presentation.utils.BaseViewModel
import com.petbulance.presentation.utils.error.ErrorDisplayType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class ReceiptCameraViewModel @Inject constructor(
    private val analyzeReceiptUseCase: AnalyzeReceiptUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow(ReceiptCameraState())
    val state: StateFlow<ReceiptCameraState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<ReceiptCameraEvent>()
    val event: SharedFlow<ReceiptCameraEvent> = _event

    fun onIntent(intent: ReceiptCameraIntent) {
        when (intent) {
            is ReceiptCameraIntent.PhotoCaptured -> onPhotoCaptured(intent.imageProxy)
            is ReceiptCameraIntent.GalleryImageSelected -> onGalleryImageSelected(
                intent.uri,
                intent.contentResolver
            )
        }
    }

    fun onPhotoCaptured(imageProxy: ImageProxy) {
        if (_state.value.isAnalyzing) {
            imageProxy.close()
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isAnalyzing = true) }

            try {
                // 1. Bitmap 변환
                val originalBitmap = imageProxy.toBitmap()

                // 2. 리사이징 (가로 1024px 기준)
                val resizedBitmap = resizeBitmap(originalBitmap, IMAGE_TARGET_WIDTH)

                // 3. 압축
                val stream = ByteArrayOutputStream()
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_COMPRESSION_QUALITY, stream)
                val imageBytes = stream.toByteArray()

                analyzeReceipt(imageBytes)
            } catch (e: Exception) {
                _event.emit(ReceiptCameraEvent.ShowError("이미지 처리에 실패했습니다."))
            } finally {
                imageProxy.close()
                _state.update { it.copy(isAnalyzing = false) }
            }
        }
    }

    fun onGalleryImageSelected(uri: Uri, contentResolver: ContentResolver) {
        if (_state.value.isAnalyzing) return

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isAnalyzing = true) }
            try {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    val imageBytes = inputStream.readBytes()
                    analyzeReceipt(imageBytes)
                } ?: run {
                    _event.emit(ReceiptCameraEvent.ShowError("이미지를 불러올 수 없습니다."))
                }
            } catch (e: Exception) {
                _event.emit(ReceiptCameraEvent.ShowError("이미지 처리에 실패했습니다."))
            } finally {
                _state.update { it.copy(isAnalyzing = false) }
            }
        }
    }

    private suspend fun analyzeReceipt(imageBytes: ByteArray) {
        // UseCase 호출로 변경
        analyzeReceiptUseCase(imageBytes)
            .onSuccess { result ->
                val uiModel = ReceiptAnalysisResultUiModel(
                    hospitalName = result.hospitalName,
                    visitDate = result.visitDate,
                    totalPrice = result.totalPrice,
                    items = result.items.map { ReceiptItemUiModel(it.name, it.price) }
                )
                _event.emit(ReceiptCameraEvent.AnalysisSuccess(uiModel))
            }.onFailure { ex ->
                _event.emit(
                    ReceiptCameraEvent.AnalysisFailed(
                        userMessage = "영수증 인식에 실패했어요",
                        exceptionMessage = ex.message,
                        displayType = ErrorDisplayType.Common
                    )
                )
            }
    }

    private fun resizeBitmap(bitmap: Bitmap, targetWidth: Int): Bitmap {
        if (bitmap.width <= targetWidth) return bitmap

        val aspectRatio = bitmap.height.toDouble() / bitmap.width.toDouble()
        val targetHeight = (targetWidth * aspectRatio).toInt()

        return bitmap.scale(targetWidth, targetHeight)
    }

    companion object {
        private const val IMAGE_TARGET_WIDTH = 1024
        private const val JPEG_COMPRESSION_QUALITY = 80
    }
}
