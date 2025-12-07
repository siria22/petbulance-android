package com.example.data.datasource.remote.network.nonfeature.app

import com.example.data.datasource.remote.network.nonfeature.app.dto.MetadataRequestDto
import com.example.data.di.network.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject


class AppApi @Inject constructor(
    private val client: HttpClient
) {
    private val baseUrl = "${BASE_URL}/app"

    suspend fun healthCheck(): HttpResponse {
        return client.get("$baseUrl/health")
    }

    suspend fun errorCheck(): HttpResponse {
        return client.get("$baseUrl/error")
    }

    suspend fun getVersion(): HttpResponse {
        return client.get("$baseUrl/version")
    }

    suspend fun getMetadata(request: MetadataRequestDto): HttpResponse {
        return client.get("$baseUrl/metadata") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }


}