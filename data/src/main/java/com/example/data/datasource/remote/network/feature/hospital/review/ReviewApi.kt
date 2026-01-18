package com.example.data.datasource.remote.network.feature.hospital.review

import com.example.data.datasource.remote.network.feature.hospital.review.dto.ReviewImageCheckReqDto
import com.example.data.datasource.remote.network.feature.hospital.review.dto.ReviewSaveReqDto
import com.example.data.di.network.AuthHttpClient
import com.example.data.di.network.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import javax.inject.Inject

class ReviewApi @Inject constructor(
    @param:AuthHttpClient private val client: HttpClient,
) {
    private val baseUrl = "$BASE_URL/receipts"

    suspend fun findHospital(hospitalName: String): HttpResponse {
        return client.get("$baseUrl/$hospitalName")
    }

    suspend fun searchReview(value: String, cursorId: Long?, size: Int): HttpResponse {
        return client.get("$baseUrl/search/$value") {
            cursorId?.let { parameter("cursorId", it) }
            parameter("size", size)
        }
    }

    suspend fun filterReview(
        region: String?,
        animalType: String?,
        receipt: Boolean?,
        cursorId: Long?,
        size: Int
    ): HttpResponse {
        return client.get("$baseUrl/filter") {
            region?.let { parameter("region", it) }
            animalType?.let { parameter("animalType", it) }
            receipt?.let { parameter("receipt", it) }
            cursorId?.let { parameter("cursorId", it) }
            parameter("size", size)
        }
    }

    suspend fun getHospitalReviews(
        hospitalId: Long,
        onlyImageReview: Boolean,
        cursorId: Long?,
        cursorRating: Double?,
        cursorLikeCount: Long?,
        size: Int,
        sortBy: String,
        sortDirection: String
    ): HttpResponse {
        return client.get("$baseUrl/reviews/$hospitalId") {
            parameter("images", onlyImageReview)
            cursorId?.let { parameter("cursorId", it) }
            cursorRating?.let { parameter("cursorRating", it) }
            cursorLikeCount?.let { parameter("cursorLikeCount", it) }
            parameter("size", size)
            parameter("sortBy", sortBy)
            parameter("sortDirection", sortDirection)
        }
    }

    suspend fun saveReview(body: ReviewSaveReqDto): HttpResponse {
        return client.post("$baseUrl/save/reviews") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }

    suspend fun checkReviewImageSave(body: ReviewImageCheckReqDto): HttpResponse {
        return client.get("$baseUrl/save/success") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }

    suspend fun getMyReviews(size: Int, cursorId: Long?): HttpResponse {
        return client.get("$baseUrl/me") {
            parameter("size", size)
            cursorId?.let { parameter("cursorId", it) }
        }
    }

    suspend fun deleteMyReviews(ids: List<Long>): HttpResponse {
        return client.delete(baseUrl) {
            ids.forEach { id ->
                parameter("ids", id)
            }
        }
    }

    suspend fun analyzeReceipt(imageBytes: ByteArray, fileName: String): HttpResponse {
        return client.submitFormWithBinaryData(
            url = baseUrl,
            formData = formData {
                append("image", imageBytes, Headers.build {
                    append(HttpHeaders.ContentType, "image/*") // 또는 "image/jpeg" 등 구체적 명시
                    append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                })
            }
        )
    }

    suspend fun uploadImage(url: String, imageBytes: ByteArray): HttpResponse {
        return client.put(url) {
            setBody(imageBytes)
        }
    }
}