package com.petbulance.presentation.screen.nonfeature.gallery

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GalleryScreen(
    maxSelectionCount: Int = 1,
    onImageSelected: (List<Uri>) -> Unit,
    onDismiss: () -> Unit,
    viewModel: GalleryViewModel = hiltViewModel() // 파일명 오타 수정 필요 (Galley -> Gallery)
) {
    val context = LocalContext.current

    // OS 버전에 따른 권한 분기 처리
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    // 권한 상태 관리
    val permissionState = rememberPermissionState(permission = permission)

    // 카메라 촬영 URI 생성 및 결과 처리
    var cameraUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success && cameraUri != null) {
                onImageSelected(listOf(cameraUri!!))
            }
        }
    )

    // 임시 파일 생성 함수 (FileProvider 필요)
    val createImageUri = remember {
        {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir = context.cacheDir
            val file = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
            val authority = "${context.packageName}.provider"
            try {
                FileProvider.getUriForFile(context, authority, file)
            } catch (e: Exception) {
                null
            }
        }
    }

    LaunchedEffect(maxSelectionCount) {
        viewModel.setMaxSelectionCount(maxSelectionCount)
    }

    // 권한 상태에 따른 UI 분기
    if (permissionState.status.isGranted) {
        GalleryContent(
            viewModel = viewModel,
            onCameraClick = {
                val uri = createImageUri()
                if (uri != null) {
                    cameraUri = uri
                    cameraLauncher.launch(uri)
                }
            },
            onComplete = {
                onImageSelected(viewModel.selectedImages.value.map { it.uri })
            },
            onDismiss = onDismiss
        )
    } else {
        // 권한 요청 화면 (빈 갤러리 대신 안내 문구 및 버튼 표시)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("갤러리 접근 권한이 필요합니다.")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { permissionState.launchPermissionRequest() }) {
                    Text("권한 허용하기")
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = onDismiss) {
                    Text("취소")
                }
            }
        }
    }
}

@Composable
fun GalleryContent(
    viewModel: GalleryViewModel,
    onCameraClick: () -> Unit,
    onComplete: () -> Unit,
    onDismiss: () -> Unit
) {
    val images = viewModel.galleryImages.collectAsLazyPagingItems()
    val selectedImages by viewModel.selectedImages.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        // 상단 바
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "취소", modifier = Modifier.clickable { onDismiss() })
            Text(text = "최근 항목", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "완료${if (selectedImages.isNotEmpty()) "(${selectedImages.size})" else ""}",
                modifier = Modifier.clickable { if (selectedImages.isNotEmpty()) onComplete() },
                color = if (selectedImages.isNotEmpty()) MaterialTheme.colorScheme.primary else Color.Gray
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(1.dp),
            horizontalArrangement = Arrangement.spacedBy(1.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            // 1. 카메라 버튼 (첫 번째 아이템)
            item {
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(Color.LightGray)
                        .clickable { onCameraClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Camera",
                            tint = Color.Black
                        )
                        Text(text = "카메라", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            // 2. 갤러리 이미지 리스트
            items(images.itemCount) { index ->
                val image = images[index]
                image?.let {
                    val isSelected = selectedImages.contains(it)
                    val selectionIndex = selectedImages.indexOf(it) + 1

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clickable { viewModel.toggleImageSelection(it) }
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(it.uri),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // 선택 인디케이터
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.4f))
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .size(24.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .border(1.dp, Color.White, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = selectionIndex.toString(),
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}