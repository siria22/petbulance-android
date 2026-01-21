package com.petbulance.data.datasource.remote.network.nonfeature.app

import com.petbulance.data.datasource.remote.network.nonfeature.app.dto.GetPresignReqDto
import com.petbulance.data.datasource.remote.network.nonfeature.app.dto.MetadataRequestDto
import com.petbulance.data.di.network.AuthHttpClient
import com.petbulance.data.di.network.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject


class AppApi @Inject constructor(
    @param:AuthHttpClient private val client: HttpClient
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
            parameter("region", request.region)
            parameter("species", request.species)
            parameter("communityCategory", request.communityCategory)
        }
    }

    suspend fun getPresignedUrl(request: GetPresignReqDto): HttpResponse {
        return client.post("$baseUrl/image/presign") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }
}