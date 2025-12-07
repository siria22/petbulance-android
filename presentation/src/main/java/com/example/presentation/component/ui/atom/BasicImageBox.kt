package com.example.presentation.component.ui.atom

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.presentation.R

/**
 * Displays an image with a circular progress indicator while it is loading.
 * Use AsyncImage from Coil.
 *
 * @param modifier The modifier to apply to this composable.
 * @param size The size of the image box. The default value is 128.dp.
 * @param galleryUri The [Uri] of the image to display. If null, a gray box will be displayed instead.
 */
@Composable
fun BasicImageBox(
    modifier: Modifier = Modifier,
    size: Dp = 128.dp,
    galleryUri: Uri?,
    errorImageResource: Int = R.drawable.broken_image,
    placeholderImageResource: Int = R.drawable.broken_image /* TODO : placeholder 이미지 */
) {
    val isUriValid = galleryUri != null && galleryUri.toString().isNotBlank()

    val dataToLoad = if (isUriValid) galleryUri else null

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(dataToLoad)
                .crossfade(true)
                .error(errorImageResource)
                .placeholder(placeholderImageResource)
                .build(),
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(4.dp)),
            contentDescription = "이미지 콘텐츠",
            contentScale = ContentScale.Crop,
        )

        val state = rememberAsyncImagePainter(model = dataToLoad).state
        if (state is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator()
        }

    }
}

@Composable
fun BasicImageBox(
    modifier: Modifier = Modifier,
    size: Dp = 128.dp,
    imageResource: Int,
    errorImageResource: Int = R.drawable.broken_image,
    placeholderImageResource: Int = R.drawable.broken_image
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageResource)
                .crossfade(true)
                .error(errorImageResource)
                .placeholder(placeholderImageResource)
                .build(),
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(4.dp)),
            contentDescription = "이미지 콘텐츠",
            contentScale = ContentScale.Crop,
        )

        val state = rememberAsyncImagePainter(model = imageResource).state
        if (state is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator()
        }
    }
}