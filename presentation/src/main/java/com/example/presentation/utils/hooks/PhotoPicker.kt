package com.example.presentation.utils.hooks

import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable

/**
 * Photo Picker를 호출. List<Uri>로 결과 받기
 *
 * @param multiple 다중 선택 여부. 기본값은 true.
 * @param maxItems 다중 선택 시 최대 선택 가능 개수. 시스템 기본값은 MediaStore.getPickImagesMaxLimit().
 * @param mediaType 선택할 미디어 타입(이미지, 비디오 등). PhotoPickerMediaType.IMAGE | VIDEO | IMAGE_AND_VIDEO
 * @param onResult 결과 콜백.
 * @return Photo Picker를 실행하는 함수: `() -> Unit`.
 */
@RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
@Composable
fun rememberPhotoPickerLauncher(
    multiple: Boolean = true,
    maxItems: Int = MediaStore.getPickImagesMaxLimit(),
    mediaType: PhotoPickerMediaType = PhotoPickerMediaType.IMAGE,
    onResult: (List<Uri>) -> Unit
): () -> Unit {

    val mediaType = when (mediaType) {
        PhotoPickerMediaType.IMAGE -> ActivityResultContracts.PickVisualMedia.ImageOnly
        PhotoPickerMediaType.VIDEO -> ActivityResultContracts.PickVisualMedia.VideoOnly
        PhotoPickerMediaType.IMAGE_AND_VIDEO -> ActivityResultContracts.PickVisualMedia.ImageAndVideo
    }

    val contract = if (multiple) {
        ActivityResultContracts.PickMultipleVisualMedia(maxItems)
    } else {
        ActivityResultContracts.PickVisualMedia()
    }

    val launcher = rememberLauncherForActivityResult(
        contract = contract,
        onResult = { result ->
            val uris = when (result) {
                is Uri -> listOf(result)
                is List<*> -> result.filterIsInstance<Uri>()
                else -> emptyList()
            }
            onResult(uris)
        }
    )

    return {
        launcher.launch(PickVisualMediaRequest(mediaType))
    }
}

enum class PhotoPickerMediaType {
    IMAGE, VIDEO, IMAGE_AND_VIDEO
}
