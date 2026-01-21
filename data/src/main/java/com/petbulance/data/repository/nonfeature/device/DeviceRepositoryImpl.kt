package com.petbulance.data.repository.nonfeature.device

import com.petbulance.data.datasource.local.system.DeviceDataSource
import com.petbulance.data.datasource.remote.network.nonfeature.device.DeviceApi
import com.petbulance.data.datasource.remote.network.nonfeature.device.dto.AddDeviceRequestDto
import com.petbulance.data.datasource.remote.network.nonfeature.device.dto.DeleteDeviceRequestDto
import com.petbulance.data.datasource.remote.network.common.safeApiCall
import com.petbulance.domain.repository.nonfeature.device.DeviceRepository
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