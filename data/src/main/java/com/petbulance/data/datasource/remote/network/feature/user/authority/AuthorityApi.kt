package com.petbulance.data.datasource.remote.network.feature.user.authority

import com.petbulance.data.di.network.AuthHttpClient
import com.petbulance.data.di.network.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

class AuthorityApi @Inject constructor(
    @param:AuthHttpClient private val client: HttpClient,
) {
    private val baseUrl = "$BASE_URL/users/authority"

    suspend fun getAuthority(): HttpResponse {
        return client.get(baseUrl)
    }

    suspend fun toggleAuthority(type: String): HttpResponse {
        return client.patch("$baseUrl/$type")
    }
}
