package com.petbulance.data.datasource.remote.network.feature.hospital.hospital

import com.petbulance.data.datasource.remote.network.feature.hospital.hospital.dto.HospitalSearchReqDto
import com.petbulance.data.di.network.AuthHttpClient
import com.petbulance.data.di.network.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

class HospitalApi @Inject constructor(
    @param:AuthHttpClient private val client: HttpClient
) {
    private val baseUrl = "${BASE_URL}/hospitals"

    suspend fun searchHospitals(
        request: HospitalSearchReqDto
    ): HttpResponse {
        return client.get(baseUrl) {
            request.q?.let { parameter("q", it) }
            request.region?.let { parameter("region", it) }
            request.lat?.let { parameter("lat", it) }
            request.lng?.let { parameter("lng", it) }
            request.bounds?.let { parameter("bounds", it) }
            request.animal?.let { parameter("animal", it) }
            request.openNow?.let { parameter("openNow", it) }
            request.sortBy?.let { parameter("sortBy", it) }
            request.size?.let { parameter("size", it) }
            request.cursorId?.let { parameter("cursorId", it) }
            request.cursorDistance?.let { parameter("cursorDistance", it) }
            request.cursorRating?.let { parameter("cursorRating", it) }
            request.cursorReviewCount?.let { parameter("cursorReviewCount", it) }
        }
    }

    suspend fun searchHospitalDetail(
        hospitalId: Long,
        lat: Double,
        lng: Double
    ): HttpResponse {
        return client.get("$baseUrl/$hospitalId") {
            parameter("lat", lat)
            parameter("lng", lng)
        }
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

