package com.example.data.datasource.remote.network.feature.community.recent

import com.example.data.di.network.AuthHttpClient
import com.example.data.di.network.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

class RecentApi @Inject constructor(
    @param:AuthHttpClient private val client: HttpClient,
) {
    private val baseUrl = "$BASE_URL/recents"

    suspend fun getRecentCommunityKeywords(): HttpResponse {
        return client.get("$baseUrl/community")
    }

    suspend fun deleteRecentCommunityKeyword(keywordId: String): HttpResponse {
        return client.delete("$baseUrl/community/$keywordId")
    }
}
