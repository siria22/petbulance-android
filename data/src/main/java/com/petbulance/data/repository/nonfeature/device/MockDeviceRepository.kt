package com.petbulance.data.repository.nonfeature.device

import com.petbulance.domain.repository.nonfeature.device.DeviceRepository
import jakarta.inject.Inject

class MockDeviceRepository @Inject constructor() : DeviceRepository {
    override suspend fun addDevice(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun deleteDevice(): Result<Unit> {
        return Result.success(Unit)
    }
}