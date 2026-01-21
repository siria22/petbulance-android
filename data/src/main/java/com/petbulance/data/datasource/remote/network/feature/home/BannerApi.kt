package com.petbulance.data.datasource.remote.network.feature.home

import com.petbulance.data.di.network.AuthHttpClient
import com.petbulance.data.di.network.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

class BannerApi @Inject constructor(
    @param:AuthHttpClient private val client: HttpClient
) {
    private val baseUrl = "${BASE_URL}/banners"

    suspend fun getHomeBanners(): HttpResponse {
        return client.get("$baseUrl/home")
    }
}