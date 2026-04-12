package com.petbulance.domain.repository.nonfeature.app

import com.petbulance.domain.model.nonfeature.app.HealthCheckResult
import com.petbulance.domain.model.nonfeature.app.MetadataResponse
import com.petbulance.domain.model.nonfeature.app.PresignFileRequest
import com.petbulance.domain.model.nonfeature.app.PresignedUrl

interface AppInfoRepository {
    suspend fun checkHealth(): Result<HealthCheckResult>
    suspend fun checkError(): Result<Unit>
    suspend fun getVersion(): Result<String>  // from server
    suspend fun getCurrentAppVersion(): Result<String> // from local
    suspend fun getMetadata(
        region: String,
        species: String,
        communityCategory: String
    ): Result<MetadataResponse>

    suspend fun getPresignedUrl(files: List<PresignFileRequest>): Result<List<PresignedUrl>>
    suspend fun uploadImage(url: String, imageBytes: ByteArray, mimeType: String): Result<Unit>
}
