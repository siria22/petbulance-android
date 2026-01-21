package com.petbulance.domain.repository.nonfeature.device

interface DeviceRepository {
    suspend fun addDevice(): Result<Unit>
    suspend fun deleteDevice(): Result<Unit>
}