package com.example.data.repository.nonfeature.device

import com.example.domain.repository.nonfeature.device.DeviceRepository

class MockDeviceRepository : DeviceRepository {
    override suspend fun addDevice(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun deleteDevice(): Result<Unit> {
        return Result.success(Unit)
    }
}