package com.petbulance.data.datasource.remote.network.feature.user.terms

import com.petbulance.data.datasource.remote.network.feature.user.terms.dto.TermsConsentRequestDto
import com.petbulance.data.di.network.AuthHttpClient
import com.petbulance.data.di.network.BASE_URL
import com.petbulance.data.di.network.DefaultHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class TermsApi @Inject constructor(
    @param:AuthHttpClient private val authClient: HttpClient,
    @param:DefaultHttpClient private val noAuthClient: HttpClient
) {
    private val baseUrl = "$BASE_URL/terms"

    suspend fun getTermsStatus(): HttpResponse {
        return authClient.get("$baseUrl/status")
    }

    suspend fun getTermsList(): HttpResponse {
        return noAuthClient.get(baseUrl)
    }

    suspend fun getTermDetail(type: String): HttpResponse {
        return noAuthClient.get("$baseUrl/$type")
    }

    suspend fun saveTermsConsent(request: TermsConsentRequestDto): HttpResponse {
        return authClient.post("$baseUrl/consents") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    suspend fun withdrawTermsConsent(type: String): HttpResponse {
        return authClient.post("$baseUrl/$type")
    }
}