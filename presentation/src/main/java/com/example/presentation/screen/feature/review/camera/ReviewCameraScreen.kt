package com.example.presentation.screen.feature.review.camera

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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.utils.hooks.PermissionHandler
import com.example.presentation.utils.nav.ScreenDestinations
import com.example.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.concurrent.Executors

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
fun ReceiptCameraScreen(
    navController: NavController,
    viewModel: ReceiptCameraViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by viewModel.state.collectAsStateWithLifecycle() // TODO : Destination으로 분리

    var showFailDialog by remember { mutableStateOf(false) }

    // CameraX UseCases
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
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
            viewModel.onGalleryImageSelected(uri, context.contentResolver)
        }
    }

    LaunchedEffect(viewModel.event) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is ReceiptCameraEvent.AnalysisSuccess -> {
                    val jsonString = Json.encodeToString(event.result)
                    val encodedJson = URLEncoder.encode(jsonString, StandardCharsets.UTF_8.toString())

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

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Camera Preview
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

                        preview.setSurfaceProvider(previewView.surfaceProvider)

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

        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .size(32.dp)
                .clickable { navController.safePopBackStack() }
                .align(Alignment.TopEnd)
        )


        Column(modifier = Modifier.fillMaxSize()) {

        }
        // 2. Overlay UI (Green Frame & Guide)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 100.dp)
        ) {
            // 녹색 가이드 프레임
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(2.dp, PetbulanceTheme.colorScheme.action.primary.default, RoundedCornerShape(12.dp))
            )
        }

        // 상단 닫기 버튼

        // 하단 컨트롤 영역
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "영수증을 영역에 맞춰 찍어주세요",
                style = MaterialTheme.typography.titleSmall,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 30.dp)
            )

            // 촬영 버튼
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable(enabled = !state.isAnalyzing) {
                        if (!state.isAnalyzing) {
                            imageCapture.takePicture(
                                ContextCompat.getMainExecutor(context),
                                object : ImageCapture.OnImageCapturedCallback() {
                                    override fun onCaptureSuccess(image: ImageProxy) {
                                        viewModel.onPhotoCaptured(image)
                                    }

                                    override fun onError(exception: ImageCaptureException) {
                                        Toast.makeText(context, "사진 촬영 실패", Toast.LENGTH_SHORT).show()
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
                        .border(4.dp, PetbulanceTheme.colorScheme.action.primary.default, CircleShape)
                )
            }
        }

        // 로딩 인디케이터
        if (state.isAnalyzing) {
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
                onDismissRequest = { showFailDialog = false },
                onRetry = {
                    showFailDialog = false
                },
                onGalleryClick = {
                    showFailDialog = false
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onManualInputClick = {
                    showFailDialog = false
                    // 빈 데이터로 Create 화면 이동
                    navController.navigate(ScreenDestinations.Review.Create.createRoute(null)) {
                        popUpTo(ScreenDestinations.Review.ReceiptCamera.route) { inclusive = true }
                    }
                }
            )
        }
    }

    BackHandler(enabled = state.isAnalyzing) {
        Toast.makeText(context, "분석 중입니다. 잠시만 기다려주세요.", Toast.LENGTH_SHORT).show()
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
private fun ReceiptCameraScreenPreview() {
    PetbulanceTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            // 1. Camera Preview Placeholder (Black Background)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            )

            // 2. Overlay UI
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 100.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            2.dp,
                            PetbulanceTheme.colorScheme.action.primary.default,
                            RoundedCornerShape(12.dp)
                        )
                )
            }

            // Close Button
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .size(32.dp)
                    .align(Alignment.TopEnd)
            )

            // Bottom Controls
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "영수증을 영역에 맞춰 찍어주세요",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 30.dp)
                )

                // Shutter Button
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .border(
                                4.dp,
                                PetbulanceTheme.colorScheme.action.primary.default,
                                CircleShape
                            )
                    )
                }
            }
        }
    }
}