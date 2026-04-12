package com.petbulance.presentation.screen.feature.community.write

import android.net.Uri

data class WritePostData(
    val mode: WritePostMode,
    val selectedAnimalType: String?,
    val selectedTopic: String?,
    val title: String,
    val content: String,
    val newImageUris: List<Uri>,
    val existingImageUrls: List<String>,
    val deletedImageUrls: List<String>,
    val isSubmitting: Boolean,
) {
    enum class WritePostMode { CREATE, EDIT }

    val isSubmitEnabled: Boolean
        get() = selectedAnimalType != null
            && selectedTopic != null
            && title.isNotBlank()
            && content.isNotBlank()
            && !isSubmitting

    val totalImageCount: Int
        get() = newImageUris.size + existingImageUrls.size

    companion object {
        val empty = WritePostData(
            mode = WritePostMode.CREATE,
            selectedAnimalType = null,
            selectedTopic = null,
            title = "",
            content = "",
            newImageUris = emptyList(),
            existingImageUrls = emptyList(),
            deletedImageUrls = emptyList(),
            isSubmitting = false
        )
    }
}
