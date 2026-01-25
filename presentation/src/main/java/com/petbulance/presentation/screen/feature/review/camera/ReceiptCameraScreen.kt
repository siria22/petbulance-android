@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package com.petbulance.presentation.screen.feature.review.camera

import android.Manifest
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeMedium
import com.petbulance.presentation.component.ui.spacingLarge
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.utils.hooks.PermissionHandler
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ReceiptCameraScreen(
    navController: NavController,
    argument: ReceiptCameraArgument,
    data: ReceiptCameraData
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var showFailDialog by remember { mutableStateOf(false) }

    // CameraX UseCases
    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    // Permission Handling
    PermissionHandler(
        permission = Manifest.permission.CAMERA,
        onPermissionGranted = { /* 권한 승인됨, 미리보기 자동 시작 */ },
        onPermissionDenied = {
            Toast.makeText(context, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            navController.safePopBackStack()
        }
    )

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            argument.intent(ReceiptCameraIntent.GalleryImageSelected(uri, context.contentResolver))
        }
    }

    LaunchedEffect(argument.event) {
        argument.event.collectLatest { event ->
            when (event) {
                is ReceiptCameraEvent.AnalysisSuccess -> {
                    val jsonString = Json.encodeToString(event.result)
                    val encodedJson =
                        URLEncoder.encode(jsonString, StandardCharsets.UTF_8.toString())

                    navController.navigate(ScreenDestinations.Review.Create.createRoute(encodedJson)) {
                        popUpTo(ScreenDestinations.Review.ReceiptCamera.route) { inclusive = true }
                    }
                }

                is ReceiptCameraEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is ReceiptCameraEvent.AnalysisFailed -> {
                    showFailDialog = true
                }
            }
        }
    }

    BackHandler(enabled = data.isAnalyzing) {
        Toast.makeText(context, "분석 중입니다. 잠시만 기다려주세요.", Toast.LENGTH_SHORT).show()
    }

    ReceiptCameraScreenContents(
        data = data,
        imageCapture = imageCapture,
        lifecycleOwner = lifecycleOwner,
        showFailDialog = showFailDialog,
        onCloseClicked = { navController.safePopBackStack() },
        onPhotoCaptured = { imageProxy ->
            argument.intent(
                ReceiptCameraIntent.PhotoCaptured(
                    imageProxy
                )
            )
        },
        onCaptureError = { Toast.makeText(context, "사진 촬영 실패", Toast.LENGTH_SHORT).show() },
        onDismissFailDialog = { showFailDialog = false },
        onRetryAnalysis = { showFailDialog = false },
        onGalleryClicked = {
            showFailDialog = false
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        onManualInputClicked = {
            showFailDialog = false
            navController.navigate(ScreenDestinations.Review.Create.createRoute(null)) {
                popUpTo(ScreenDestinations.Review.ReceiptCamera.route) { inclusive = true }
            }
        }
    )
}

@Composable
private fun ReceiptCameraScreenContents(
    data: ReceiptCameraData,
    imageCapture: ImageCapture?,
    lifecycleOwner: LifecycleOwner?,
    showFailDialog: Boolean,
    onCloseClicked: () -> Unit,
    onPhotoCaptured: (ImageProxy) -> Unit,
    onCaptureError: () -> Unit,
    onDismissFailDialog: () -> Unit,
    onRetryAnalysis: () -> Unit,
    onGalleryClicked: () -> Unit,
    onManualInputClicked: () -> Unit
) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Camera Preview
        if (imageCapture != null && lifecycleOwner != null) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    }.also { previewView ->
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder().build()
                            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                            preview.surfaceProvider = previewView.surfaceProvider

                            try {
                                cameraProvider.unbindAll()
                                cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    cameraSelector,
                                    preview,
                                    imageCapture
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }, ContextCompat.getMainExecutor(ctx))
                    }
                }
            )
        } else {
            // Preview Placeholder
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    BasicIcon(
                        iconResource = IconResource.Vector(Icons.Default.Close),
                        contentDescription = "close",
                        size = iconSizeMedium,
                        tint = colorScheme.icon.inverse,
                        modifier = Modifier.clickable { onCloseClicked() }
                    )
                }
            }

            // 2. Overlay UI (Green Frame & Guide)
            Column (
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = spacingMedium, vertical = spacingLarge),
                verticalArrangement = Arrangement.spacedBy(spacingSmall)
            ) {
                Text(
                    text = "영수증을 영역에 맞춰 찍어주세요",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                        .border(
                            2.dp,
                            colorScheme.action.primary.default
                        )
                )
                Text(
                    text = "빛 반사나 구김이 없을수록 정확하게 인식돼요.",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 하단 컨트롤 영역
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // 촬영 버튼
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(colorScheme.action.primary.default)
                        .clickable(enabled = !data.isAnalyzing) {
                            if (!data.isAnalyzing && imageCapture != null) {
                                imageCapture.takePicture(
                                    ContextCompat.getMainExecutor(context),
                                    object : ImageCapture.OnImageCapturedCallback() {
                                        override fun onCaptureSuccess(image: ImageProxy) {
                                            onPhotoCaptured(image)
                                        }

                                        override fun onError(exception: ImageCaptureException) {
                                            onCaptureError()
                                        }
                                    }
                                )
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .border(
                                6.dp,
                                Color.Black,
                                CircleShape
                            )
                    )
                }
            }
        }

        // 로딩 인디케이터
        if (data.isAnalyzing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(enabled = false) {},
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PetbulanceTheme.colorScheme.action.primary.default)
            }
        }

        if (showFailDialog) {
            ReceiptAnalysisFailDialog(
                onDismissRequest = onDismissFailDialog,
                onRetry = onRetryAnalysis,
                onGalleryClick = onGalleryClicked,
                onManualInputClick = onManualInputClicked
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
private fun ReceiptCameraScreenPreview() {
    PetbulanceTheme {
        ReceiptCameraScreenContents(
            data = ReceiptCameraData(isAnalyzing = false),
            imageCapture = null,
            lifecycleOwner = null,
            showFailDialog = false,
            onCloseClicked = {},
            onPhotoCaptured = {},
            onCaptureError = {},
            onDismissFailDialog = {},
            onRetryAnalysis = {},
            onGalleryClicked = {},
            onManualInputClicked = {}
        )
    }
}