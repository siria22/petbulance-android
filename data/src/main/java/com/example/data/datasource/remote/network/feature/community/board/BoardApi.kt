package com.example.data.datasource.remote.network.feature.community.board

import com.example.data.di.network.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject


class BoardApi @Inject constructor(
    private val client: HttpClient
) {
    private val baseUrl = "${BASE_URL}/boards"

    suspend fun boardList(): HttpResponse {
        return client.get(baseUrl)
    }

}