package com.petbulance.data.datasource.remote.network.feature.hospital.history

import com.petbulance.data.datasource.remote.network.feature.hospital.history.dto.RecentHospitalSaveReqDto
import com.petbulance.data.datasource.remote.network.feature.hospital.history.dto.ViewedHospitalSaveReqDto
import com.petbulance.data.di.network.AuthHttpClient
import com.petbulance.data.di.network.BASE_URL
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import javax.inject.Inject

class HistoryApi @Inject constructor(
    @param:AuthHttpClient private val client: HttpClient,
) {
    private val baseUrl = "$BASE_URL/recents"

    suspend fun getRecentKeywords(): HttpResponse {
        return client.get("$baseUrl/hospitals")
    }

    suspend fun saveRecentKeyword(keyword: String): HttpResponse {
        return client.post("$baseUrl/hospitals") {
            contentType(ContentType.Application.Json)
            setBody(RecentHospitalSaveReqDto(keyword))
        }
    }

    suspend fun deleteRecentKeyword(keywordId: Long): HttpResponse {
        return client.delete("$baseUrl/hospitals/$keywordId")
    }

    suspend fun saveViewedHospital(hospitalId: Long): HttpResponse {
        return client.post("$baseUrl/viewed") {
            contentType(ContentType.Application.Json)
            setBody(ViewedHospitalSaveReqDto(hospitalId))
        }
    }

    suspend fun getViewedHospitals(): HttpResponse {
        return client.get("$baseUrl/viewed")
    }

    suspend fun deleteViewedHospital(hospitalId: Long): HttpResponse {
        return client.delete("$baseUrl/viewed/$hospitalId")
    }
}