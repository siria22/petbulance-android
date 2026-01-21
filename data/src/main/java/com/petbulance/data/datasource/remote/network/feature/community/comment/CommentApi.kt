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
        keyword: String,
        searchScope: String,
        lastCommentId: Long?,
        pageSize: Int,
        category: List<String>?,
        boardId: Long?
    ): HttpResponse {
        return client.get("$baseUrl/search") {
            parameter("keyword", keyword)
            parameter("searchScope", searchScope)
            lastCommentId?.let { parameter("lastCommentId", it) }
            parameter("pageSize", pageSize)
            category?.forEach { parameter("category", it) }
            boardId?.let { parameter("boardId", it) }
        }
    }

    suspend fun getMyCommentList(
        keyword: String?,
        lastCommentId: Long?,
        pageSize: Int
    ): HttpResponse {
        return client.get("$baseUrl/me") {
            keyword?.let { parameter("keyword", it) }
            lastCommentId?.let { parameter("lastCommentId", it) }
            parameter("pageSize", pageSize)
        }
    }
}