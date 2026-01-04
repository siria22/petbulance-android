package com.example.domain.repository.nonfeature.app

import com.example.domain.model.nonfeature.app.HealthCheckResult
import com.example.domain.model.nonfeature.app.MetadataResponse

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
}