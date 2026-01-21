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

class PostApi(
    @param:AuthHttpClient private val client: HttpClient,
) {
    private val baseUrl = "${BASE_URL}/posts"

    suspend fun createPost(reqDto: CreatePostReqDto): HttpResponse {
        return client.post(baseUrl) {
            contentType(ContentType.Application.Json)
            setBody(reqDto)
        }
    }

    suspend fun getPostDetail(postId: Long): HttpResponse {
        return client.get("$baseUrl/$postId")
    }

    suspend fun updatePost(postId: Long, reqDto: UpdatePostReqDto): HttpResponse {
        return client.put("$baseUrl/$postId") {
            contentType(ContentType.Application.Json)
            setBody(reqDto)
        }
    }

    suspend fun deletePost(postId: Long): HttpResponse {
        return client.delete("$baseUrl/$postId")
    }

    suspend fun getPostList(
        boardId: Long? = null,
        category: String? = null,
        sort: String = "popular",
        lastPostId: Long? = null,
        pageSize: Int = 10
    ): HttpResponse {
        return client.get(baseUrl) {
            boardId?.let { parameter("boardId", it) }
            category?.let { parameter("category", it) }
            lastPostId?.let { parameter("lastPostId", it) }
            parameter("sort", sort)
            parameter("pageSize", pageSize)
        }
    }

    suspend fun getPostSearchList(
        boardId: Long? = null,
        categories: List<String>? = null,
        sort: String = "popular",
        lastPostId: Long? = null,
        pageSize: Int = 10,
        searchKeyword: String? = null,
        searchScope: String = "title_content"
    ): HttpResponse {
        return client.get("$baseUrl/search") {
            boardId?.let { parameter("boardId", it) }
            lastPostId?.let { parameter("lastPostId", it) }
            searchKeyword?.let { parameter("searchKeyword", it) }

            parameter("sort", sort)
            parameter("pageSize", pageSize)
            parameter("searchScope", searchScope)

            categories?.forEach { category ->
                parameter("category", category)
            }
        }
    }

    suspend fun getMyPostList(
        keyword: String? = null,
        lastPostId: Long? = null,
        pageSize: Int = 10
    ): HttpResponse {
        return client.get("$baseUrl/me") {
            keyword?.let { parameter("keyword", it) }
            lastPostId?.let { parameter("lastPostId", it) }
            parameter("pageSize", pageSize)
        }
    }

    suspend fun likePost(postId: Long): HttpResponse {
        return client.post("$baseUrl/$postId/likes")
    }

    suspend fun unlikePost(postId: Long): HttpResponse {
        return client.delete("$baseUrl/$postId/likes")
    }

    suspend fun createComment(
        postId: Long,
        reqDto: CreatePostCommentReqDto
    ): HttpResponse {
        return client.post("$baseUrl/$postId/comments") {
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
        return client.get("$baseUrl/$postId/comments") {
            lastParentCommentId?.let { parameter("lastParentCommentId", it) }
            lastCommentId?.let { parameter("lastCommentId", it) }
            parameter("pageSize", pageSize)
        }
    }
}