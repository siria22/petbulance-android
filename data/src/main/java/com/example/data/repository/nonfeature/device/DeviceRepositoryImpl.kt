package com.example.data.repository.nonfeature.device

import com.example.data.datasource.local.system.DeviceDataSource
import com.example.data.datasource.remote.network.nonfeature.device.DeviceApi
import com.example.data.datasource.remote.network.nonfeature.device.dto.AddDeviceRequestDto
import com.example.data.datasource.remote.network.nonfeature.device.dto.DeleteDeviceRequestDto
import com.example.data.datasource.remote.network.common.safeApiCall
import com.example.domain.repository.nonfeature.device.DeviceRepository
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(
    private val api: DeviceApi,
    private val deviceDataSource: DeviceDataSource
) : DeviceRepository {

    override suspend fun addDevice(): Result<Unit> {
        val request = AddDeviceRequestDto(
            fcmToken = deviceDataSource.getFcmToken(),
            deviceOs = deviceDataSource.getDeviceOs()
        )

        return safeApiCall<String>(path = "/device") {
            api.addDevice(request)
        }.map { }
    }

    override suspend fun deleteDevice(): Result<Unit> {
        val request = DeleteDeviceRequestDto(
            fcmToken = deviceDataSource.getFcmToken()
        )

        return safeApiCall<String>(path = "/device") {
            api.deleteDevice(request)
        }.map { }
    }
}