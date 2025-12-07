package com.example.data.datasource.remote.network.nonfeature.device

import com.example.data.datasource.remote.network.nonfeature.device.dto.AddDeviceRequestDto
import com.example.data.datasource.remote.network.nonfeature.device.dto.DeleteDeviceRequestDto
import com.example.data.di.network.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class DeviceApi @Inject constructor(
    private val client: HttpClient
) {
    private val baseUrl = "${BASE_URL}/device"

    suspend fun addDevice(
        request: AddDeviceRequestDto
    ): HttpResponse {
        return client.post(baseUrl) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    suspend fun deleteDevice(
        request: DeleteDeviceRequestDto
    ): HttpResponse {
        return client.delete(baseUrl) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }
}