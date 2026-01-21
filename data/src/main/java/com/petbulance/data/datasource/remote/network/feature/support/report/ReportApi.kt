package com.petbulance.data.datasource.remote.network.feature.support.report

import com.petbulance.data.datasource.remote.network.feature.support.report.dto.ReportCreateReqDto
import com.petbulance.data.di.network.AuthHttpClient
import com.petbulance.data.di.network.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class ReportApi @Inject constructor(
    @param:AuthHttpClient private val client: HttpClient
) {
    private val baseUrl = "${BASE_URL}/reports"

    suspend fun createReport(reqDto: ReportCreateReqDto): HttpResponse {
        return client.post(baseUrl) {
            contentType(ContentType.Application.Json)
            setBody(reqDto)
        }
    }
}