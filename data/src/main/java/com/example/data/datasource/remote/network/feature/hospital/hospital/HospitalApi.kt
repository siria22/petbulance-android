package com.example.data.datasource.remote.network.feature.hospital.hospital

import com.example.data.datasource.remote.network.feature.hospital.hospital.dto.HospitalSearchReqDto
import com.example.data.di.network.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

class HospitalApi @Inject constructor(
    private val client: HttpClient
) {
    private val baseUrl = "${BASE_URL}/hospitals"

    suspend fun searchHospitals(
        request: HospitalSearchReqDto,
        size: Int,
        page: Int
    ): HttpResponse {
        return client.get("$baseUrl/search") {
            request.q?.let { parameter("q", it) }
            request.region?.let { parameter("region", it) }
            request.lat?.let { parameter("lat", it) }
            request.lng?.let { parameter("lng", it) }
            request.bounds?.let { parameter("bounds", it) }
            request.animal?.let { parameter("animal", it) }
            request.openNow?.let { parameter("openNow", it) }

            parameter("page", page)
            parameter("size", size)
        }
    }

    suspend fun searchHospitalDetail(
        hospitalId: Long
    ): HttpResponse {
        return client.get("$baseUrl/$hospitalId")
    }

    suspend fun searchHospitalCard(
        hospitalId: Long,
        lat: Double,
        lng: Double
    ): HttpResponse {
        return client.get("$baseUrl/card/$hospitalId") {
            parameter("lat", lat)
            parameter("lng", lng)
        }
    }
}

