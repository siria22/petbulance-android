package com.petbulance.presentation.screen.nonfeature.gallery

import android.net.Uri

data class GalleryImage(
    val id: Long,
    val uri: Uri,
    val name: String,
    val dateTaken: Long
)