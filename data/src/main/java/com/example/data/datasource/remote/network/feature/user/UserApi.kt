package com.example.data.datasource.remote.network.feature.user

import com.example.data.datasource.remote.network.feature.user.dto.CheckProfileImageReqDto
import com.example.data.datasource.remote.network.feature.user.dto.NicknameSaveRequestDto
import com.example.data.datasource.remote.network.feature.user.dto.NotificationSettingReqDto
import com.example.data.datasource.remote.network.feature.user.dto.ProfileImageUpdateReqDto
import com.example.data.datasource.remote.network.feature.user.dto.SocialConnectRequestDto
import com.example.data.di.network.BASE_URL
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import javax.inject.Inject

class UserApi @Inject constructor(
    private val client: HttpClient,
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
        // GET 요청이지만 Body를 포함 (서버 스펙 준수)
        return client.get("$baseUrl/profile/success") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    suspend fun getMyInfo(): HttpResponse {
        return client.get("$baseUrl/me")
    }

    suspend fun updateNotificationSettings(request: NotificationSettingReqDto): HttpResponse {
        return client.patch("$baseUrl/settings/notification") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }
}