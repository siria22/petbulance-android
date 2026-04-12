package com.petbulance.data.repository.nonfeature.app

import android.content.Context
import android.net.Uri
import com.petbulance.domain.repository.nonfeature.app.ContentFileData
import com.petbulance.domain.repository.nonfeature.app.ContentFileReader
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ContentFileReaderImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ContentFileReader {

    override suspend fun readBytes(uriString: String): ContentFileData? {
        return try {
            val uri = Uri.parse(uriString)
            val contentResolver = context.contentResolver
            val mimeType = contentResolver.getType(uri) ?: "image/jpeg"
            val bytes = contentResolver.openInputStream(uri)?.use { it.readBytes() }
                ?: return null
            ContentFileData(bytes = bytes, mimeType = mimeType)
        } catch (e: Exception) {
            null
        }
    }
}
