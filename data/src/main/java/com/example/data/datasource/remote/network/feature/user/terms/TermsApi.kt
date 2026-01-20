package com.example.data.datasource.remote.network.feature.user.terms

import com.example.data.datasource.remote.network.feature.user.terms.dto.TermsConsentRequestDto
import com.example.data.di.network.AuthHttpClient
import com.example.data.di.network.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class TermsApi @Inject constructor(
    @param:AuthHttpClient private val client: HttpClient
) {
    private val baseUrl = "$BASE_URL/terms"

    suspend fun getTermsStatus(): HttpResponse {
        return client.get("$baseUrl/status")
    }

    suspend fun getTermsList(): HttpResponse {
        return client.get(baseUrl)
    }

    suspend fun getTermDetail(type: String): HttpResponse {
        return client.get("$baseUrl/$type")
    }

    suspend fun saveTermsConsent(request: TermsConsentRequestDto): HttpResponse {
        return client.post("$baseUrl/consents") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    suspend fun withdrawTermsConsent(type: String): HttpResponse {
        return client.delete("$baseUrl/$type")
    }
}