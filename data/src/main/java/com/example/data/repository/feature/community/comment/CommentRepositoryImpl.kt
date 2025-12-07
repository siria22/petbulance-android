package com.example.data.repository.feature.community.comment

import com.example.data.datasource.remote.network.feature.community.comment.CommentApi
import com.example.data.datasource.remote.network.feature.community.comment.dto.DelCommentResDto
import com.example.data.datasource.remote.network.feature.community.comment.dto.PagingMyCommentListResDto
import com.example.data.datasource.remote.network.feature.community.comment.dto.PostCommentResDto
import com.example.data.datasource.remote.network.feature.community.comment.dto.SearchPostCommentListResDto
import com.example.data.datasource.remote.network.common.safeApiCall
import com.example.data.mapper.feature.community.toDomain
import com.example.data.mapper.feature.community.toDto
import com.example.domain.model.feature.community.comment.DelComment
import com.example.domain.model.feature.community.comment.MyCommentList
import com.example.domain.model.feature.community.comment.PostComment
import com.example.domain.model.feature.community.comment.SearchPostCommentListRes
import com.example.domain.model.feature.community.comment.UpdatePostCommentReq
import com.example.domain.repository.feature.community.CommentRepository
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val api: CommentApi
) : CommentRepository {

    override suspend fun updatePostComment(
        commentId: Long,
        request: UpdatePostCommentReq
    ): Result<PostComment> {
        return safeApiCall<PostCommentResDto>(path = "/comments/$commentId") {
            api.updatePostComment(commentId, request.toDto())
        }.map { it.toDomain() }
    }

    override suspend fun deletePostComment(
        commentId: Long
    ): Result<DelComment> {
        return safeApiCall<DelCommentResDto>(path = "/comments/$commentId") {
            api.deletePostComment(commentId)
        }.map { it.toDomain() }
    }

    override suspend fun searchPostCommentList(
        keyword: String,
        searchScope: String,
        lastCommentId: Long?,
        pageSize: Int,
        category: List<String>?,
        boardId: Long?
    ): Result<SearchPostCommentListRes> {
        return safeApiCall<SearchPostCommentListResDto>(path = "/comments/search") {
            api.searchPostCommentList(
                keyword,
                searchScope,
                lastCommentId,
                pageSize,
                category,
                boardId
            )
        }.map { it.toDomain() }
    }

    override suspend fun getMyCommentList(
        keyword: String?,
        lastCommentId: Long?,
        pageSize: Int
    ): Result<MyCommentList> {
        return safeApiCall<PagingMyCommentListResDto>(path = "/comments/me") {
            api.getMyCommentList(keyword, lastCommentId, pageSize)
        }.map { it.toDomain() }
    }
}