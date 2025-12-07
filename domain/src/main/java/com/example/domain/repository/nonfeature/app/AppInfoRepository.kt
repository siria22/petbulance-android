package com.example.domain.repository.nonfeature.app

import com.example.domain.model.nonfeature.app.HealthCheckResult
import com.example.domain.model.nonfeature.app.MetadataResponse

interface AppInfoRepository {
    suspend fun checkHealth(): Result<HealthCheckResult>
    suspend fun checkError(): Result<Unit>
    suspend fun getVersion(): Result<String>
    suspend fun getMetadata(
        region: String,
        species: String,
        communityCategory: String
    ): Result<MetadataResponse>
}