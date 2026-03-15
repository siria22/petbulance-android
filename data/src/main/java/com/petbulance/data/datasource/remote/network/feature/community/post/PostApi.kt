package com.petbulance.data.datasource.remote.network.feature.community.post

import com.petbulance.data.datasource.remote.network.feature.community.post.dto.comment.CreatePostCommentReqDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.post.CreatePostReqDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.post.UpdatePostReqDto
import com.petbulance.data.di.network.AuthHttpClient
import com.petbulance.data.di.network.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class PostApi @Inject constructor(
    @param:AuthHttpClient private val authClient: HttpClient
) {
    private val baseUrl = "${BASE_URL}/posts"

    suspend fun createPost(reqDto: CreatePostReqDto): HttpResponse {
        return authClient.post(baseUrl) {
            contentType(ContentType.Application.Json)
            setBody(reqDto)
        }
    }

    suspend fun getPostDetail(postId: Long): HttpResponse {
        return authClient.get("$baseUrl/$postId")
    }

    suspend fun updatePost(postId: Long, reqDto: UpdatePostReqDto): HttpResponse {
        return authClient.put("$baseUrl/$postId") {
            contentType(ContentType.Application.Json)
            setBody(reqDto)
        }
    }

    suspend fun deletePost(postId: Long): HttpResponse {
        return authClient.delete("$baseUrl/$postId")
    }

    suspend fun getPostList(
        type: String? = null,
        topic: String? = null,
        sort: String = "latest",
        lastPostId: Long? = null,
        pageSize: Int = 10
    ): HttpResponse {
        return authClient.get(baseUrl) {
            type?.let { parameter("type", it) }
            topic?.let { parameter("topic", it) }
            parameter("sort", sort)
            lastPostId?.let { parameter("lastPostId", it) }
            parameter("pageSize", pageSize)
        }
    }

    suspend fun getPostSearchList(
        type: String? = null,
        topic: String? = null,
        sort: String = "latest",
        lastPostId: Long? = null,
        pageSize: Int = 20,
        searchKeyword: String,
        searchScope: String = "title_content"
    ): HttpResponse {
        return authClient.get("$baseUrl/search") {
            type?.let { parameter("type", it) }
            topic?.let { parameter("topic", it) }
            parameter("sort", sort)
            lastPostId?.let { parameter("lastPostId", it) }
            parameter("pageSize", pageSize)
            parameter("searchKeyword", searchKeyword)
            parameter("searchScope", searchScope)
        }
    }

    suspend fun getMyPostList(
        keyword: String? = null,
        lastPostId: Long? = null,
        pageSize: Int = 10
    ): HttpResponse {
        return authClient.get("$baseUrl/me") {
            keyword?.let { parameter("keyword", it) }
            lastPostId?.let { parameter("lastPostId", it) }
            parameter("pageSize", pageSize)
        }
    }

    suspend fun likePost(postId: Long): HttpResponse {
        return authClient.post("$baseUrl/$postId/likes")
    }

    suspend fun unlikePost(postId: Long): HttpResponse {
        return authClient.delete("$baseUrl/$postId/likes")
    }

    suspend fun createComment(
        postId: Long,
        reqDto: CreatePostCommentReqDto
    ): HttpResponse {
        return authClient.post("$baseUrl/$postId/comments") {
            contentType(ContentType.Application.Json)
            setBody(reqDto)
        }
    }

    suspend fun getCommentList(
        postId: Long,
        lastParentCommentId: Long? = null,
        lastCommentId: Long? = null,
        pageSize: Int = 15
    ): HttpResponse {
        return authClient.get("$baseUrl/$postId/comments") {
            lastParentCommentId?.let { parameter("lastParentCommentId", it) }
            lastCommentId?.let { parameter("lastCommentId", it) }
            parameter("pageSize", pageSize)
        }
    }
}