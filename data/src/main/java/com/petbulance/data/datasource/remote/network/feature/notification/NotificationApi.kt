package com.petbulance.data.datasource.remote.network.feature.notification

import com.petbulance.data.di.network.AuthHttpClient
import com.petbulance.data.di.network.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

class NotificationApi @Inject constructor(
    @param:AuthHttpClient private val client: HttpClient,
) {
    private val baseUrl = "$BASE_URL/notifications"

    suspend fun getNotifications(
        lastNotificationId: Long? = null,
        pageSize: Int = 20
    ): HttpResponse {
        return client.get(baseUrl) {
            lastNotificationId?.let { parameter("lastNotificationId", it) }
            parameter("pageSize", pageSize)
        }
    }

    suspend fun readAllNotifications(): HttpResponse {
        return client.patch("$baseUrl/read-all")
    }

    suspend fun deleteAllNotifications(): HttpResponse {
        return client.delete(baseUrl)
    }
}
