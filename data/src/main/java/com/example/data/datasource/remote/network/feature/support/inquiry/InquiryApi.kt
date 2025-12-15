package com.example.data.datasource.remote.network.feature.support.inquiry

import com.example.data.datasource.remote.network.feature.support.inquiry.dto.InquiryReqDto
import com.example.data.di.network.AuthHttpClient
import com.example.data.di.network.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class InquiryApi @Inject constructor(
    @param:AuthHttpClient private val client: HttpClient
) {
    private val baseUrl = "${BASE_URL}/inquiries"

    suspend fun createInquiry(inquiryReqDto: InquiryReqDto): HttpResponse {
        return client.post(baseUrl) {
            contentType(ContentType.Application.Json)
            setBody(inquiryReqDto)
        }
    }
}