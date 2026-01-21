package com.petbulance.data.datasource.remote.network.feature.support.qna

import com.petbulance.data.datasource.remote.network.feature.support.qna.dto.CreateQnaReqDto
import com.petbulance.data.datasource.remote.network.feature.support.qna.dto.UpdateQnaReqDto
import com.petbulance.data.di.network.AuthHttpClient
import com.petbulance.data.di.network.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class QnaApi(
    @param:AuthHttpClient private val client: HttpClient,
) {
    private val baseUrl = "${BASE_URL}/qna"

    suspend fun createQna(reqDto: CreateQnaReqDto): HttpResponse {
        return client.post(baseUrl) {
            contentType(ContentType.Application.Json)
            setBody(reqDto)
        }
    }

    suspend fun updateQna(qnaId: Long, reqDto: UpdateQnaReqDto): HttpResponse {
        return client.put("$baseUrl/$qnaId") {
            contentType(ContentType.Application.Json)
            setBody(reqDto)
        }
    }

    suspend fun deleteQna(qnaId: Long): HttpResponse {
        return client.delete("$baseUrl/$qnaId")
    }
}