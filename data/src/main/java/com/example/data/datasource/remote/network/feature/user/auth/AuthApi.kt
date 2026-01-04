package com.example.data.datasource.remote.network.feature.user.auth

import com.example.data.datasource.remote.network.feature.user.auth.dto.SocialLoginRequestDto
import com.example.data.di.network.AuthHttpClient
import com.example.data.di.network.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class AuthApi @Inject constructor(
    @param:AuthHttpClient private val client: HttpClient
) {
    private val baseUrl = "$BASE_URL/auth"

    suspend fun socialLogin(request: SocialLoginRequestDto): HttpResponse {
        return client.post("$baseUrl/social/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    suspend fun logout(): HttpResponse {
        return client.get("$baseUrl/logout")
    }

    suspend fun refresh(): HttpResponse {
        return client.get("$baseUrl/refresh")
    }
}