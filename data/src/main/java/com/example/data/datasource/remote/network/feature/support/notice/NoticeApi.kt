package com.example.data.datasource.remote.network.feature.support.notice

import com.example.data.di.network.AuthHttpClient
import com.example.data.di.network.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse

class NoticeApi(
    @param:AuthHttpClient private val client: HttpClient,
) {
    private val baseUrl = "${BASE_URL}/notices"

    suspend fun getNoticeList(
        lastNoticeId: Long? = null,
        pageSize: Int = 10
    ): HttpResponse {
        return client.get(baseUrl) {
            lastNoticeId?.let { parameter("lastNoticeId", it) }
            parameter("pageSize", pageSize)
        }
    }

    suspend fun getNoticeDetail(noticeId: Long): HttpResponse {
        return client.get("$baseUrl/$noticeId")
    }
}