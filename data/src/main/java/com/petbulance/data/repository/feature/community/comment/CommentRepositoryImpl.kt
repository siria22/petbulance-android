package com.petbulance.data.repository.feature.community.comment

import com.petbulance.data.datasource.remote.network.feature.community.comment.CommentApi
import com.petbulance.data.datasource.remote.network.feature.community.comment.dto.CommentResDto
import com.petbulance.data.datasource.remote.network.feature.community.comment.dto.DelCommentResDto
import com.petbulance.data.datasource.remote.network.feature.community.comment.dto.PagingMyCommentListResDto
import com.petbulance.data.datasource.remote.network.feature.community.comment.dto.SearchPostCommentListResDto
import com.petbulance.data.datasource.remote.network.common.safeApiCall
import com.petbulance.data.mapper.feature.community.toDomain
import com.petbulance.data.mapper.feature.community.toDto
import com.petbulance.domain.model.feature.community.comment.DelComment
import com.petbulance.domain.model.feature.community.comment.MyCommentList
import com.petbulance.domain.model.feature.community.comment.PostComment
import com.petbulance.domain.model.feature.community.comment.SearchPostCommentListRes
import com.petbulance.domain.model.feature.community.comment.UpdatePostCommentReq
import com.petbulance.domain.repository.feature.community.CommentRepository
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val api: CommentApi
) : CommentRepository {

    override suspend fun updatePostComment(
        commentId: Long,
        request: UpdatePostCommentReq
    ): Result<PostComment> {
        return safeApiCall<CommentResDto>(path = "/comments/$commentId") {
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
        searchKeyword: String,
        searchScope: String,
        lastCommentId: Long?,
        pageSize: Int,
        topic: String?,
        type: String?
    ): Result<SearchPostCommentListRes> {
        return safeApiCall<SearchPostCommentListResDto>(path = "/comments/search") {
            api.searchPostCommentList(
                searchKeyword,
                searchScope,
                lastCommentId,
                pageSize,
                topic,
                type
            )
        }.map { it.toDomain() }
    }

    override suspend fun getMyCommentList(
        searchKeyword: String?,
        lastCommentId: Long?,
        pageSize: Int
    ): Result<MyCommentList> {
        return safeApiCall<PagingMyCommentListResDto>(path = "/comments/me") {
            api.getMyCommentList(searchKeyword, lastCommentId, pageSize)
        }.map { it.toDomain() }
    }
}