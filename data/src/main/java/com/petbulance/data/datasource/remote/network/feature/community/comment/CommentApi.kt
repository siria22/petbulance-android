package com.petbulance.data.datasource.remote.network.feature.community.comment

import com.petbulance.data.datasource.remote.network.feature.community.comment.dto.UpdatePostCommentReqDto
import com.petbulance.data.di.network.AuthHttpClient
import com.petbulance.data.di.network.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class CommentApi @Inject constructor(
    @param:AuthHttpClient private val client: HttpClient
) {
    private val baseUrl = "${BASE_URL}/comments"

    suspend fun updatePostComment(
        commentId: Long,
        dto: UpdatePostCommentReqDto
    ): HttpResponse {
        return client.patch("$baseUrl/$commentId") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }
    }

    suspend fun deletePostComment(
        commentId: Long
    ): HttpResponse {
        return client.delete("$baseUrl/$commentId")
    }

    suspend fun searchPostCommentList(
        searchKeyword: String,
        searchScope: String,
        lastCommentId: Long?,
        pageSize: Int,
        topic: String?,
        type: String?
    ): HttpResponse {
        return client.get("$baseUrl/search") {
            parameter("searchKeyword", searchKeyword)
            parameter("searchScope", searchScope)
            lastCommentId?.let { parameter("lastCommentId", it) }
            parameter("pageSize", pageSize)
            topic?.let { parameter("topic", it) }
            type?.let { parameter("type", it) }
        }
    }

    suspend fun getMyCommentList(
        searchKeyword: String?,
        lastCommentId: Long?,
        pageSize: Int
    ): HttpResponse {
        return client.get("$baseUrl/me") {
            searchKeyword?.let { parameter("keyword", it) }
            lastCommentId?.let { parameter("lastCommentId", it) }
            parameter("pageSize", pageSize)
        }
    }
}