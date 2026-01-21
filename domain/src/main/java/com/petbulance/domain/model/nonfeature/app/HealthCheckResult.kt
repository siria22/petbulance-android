package com.petbulance.domain.model.nonfeature.app

data class HealthCheckResult(
    val message: String,
    val isHealthy: Boolean = true
)