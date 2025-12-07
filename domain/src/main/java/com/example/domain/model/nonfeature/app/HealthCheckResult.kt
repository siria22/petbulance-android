package com.example.domain.model.nonfeature.app

data class HealthCheckResult(
    val message: String,
    val isHealthy: Boolean = true
)