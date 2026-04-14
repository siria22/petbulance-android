package com.petbulance.data.datasource.remote.network.feature.user.user

import com.petbulance.data.datasource.remote.network.feature.user.user.dto.CheckProfileImageReqDto
import com.petbulance.data.datasource.remote.network.feature.user.user.dto.NicknameSaveRequestDto
import com.petbulance.data.datasource.remote.network.feature.user.user.dto.NotificationSettingReqDto
import com.petbulance.data.datasource.remote.network.feature.user.user.dto.ProfileImageUpdateReqDto
import com.petbulance.data.datasource.remote.network.feature.user.user.dto.SocialConnectRequestDto
import com.petbulance.data.di.network.AuthHttpClient
import com.petbulance.data.di.network.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class UserApi @Inject constructor(
    @param:AuthHttpClient private val client: HttpClient,
) {
    private val baseUrl = "$BASE_URL/users"

    suspend fun checkNickname(nickname: String): HttpResponse {
        return client.get("$baseUrl/nickname") {
            parameter("nickname", nickname)
        }
    }

    suspend fun saveNickname(request: NicknameSaveRequestDto): HttpResponse {
        return client.post("$baseUrl/nickname") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    suspend fun updateNickname(request: NicknameSaveRequestDto): HttpResponse {
        return client.patch("$baseUrl/nickname") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    suspend fun connectSocialAccount(request: SocialConnectRequestDto): HttpResponse {
        // 경로 주의: Controller의 RequestMapping에 따라 "/social/connect" 또는 "$baseUrl/social/connect"
        return client.post("$baseUrl/social/connect") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    suspend fun disconnectSocialAccount(platform: String): HttpResponse {
        return client.delete("$baseUrl/social/disconnect/$platform")
    }

    suspend fun updateProfileImage(request: ProfileImageUpdateReqDto): HttpResponse {
        return client.patch("$baseUrl/profile") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    suspend fun checkProfileImageUpdate(request: CheckProfileImageReqDto): HttpResponse {
        return client.post("$baseUrl/profile/success") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    suspend fun deleteAccount(): HttpResponse {
        return client.delete(baseUrl)
    }

    suspend fun getMyInfo(): HttpResponse {
        return client.get("$BASE_URL/users/me")
    }

    suspend fun getNotificationSettings(): HttpResponse {
        return client.get("$baseUrl/settings/notification")
    }

    suspend fun updateNotificationSettings(request: NotificationSettingReqDto): HttpResponse {
        return client.patch("$baseUrl/settings/notification") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }
}