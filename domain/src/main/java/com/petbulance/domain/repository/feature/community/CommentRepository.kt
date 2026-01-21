package com.petbulance.domain.repository.feature.community

import com.petbulance.domain.model.feature.community.comment.DelComment
import com.petbulance.domain.model.feature.community.comment.MyCommentList
import com.petbulance.domain.model.feature.community.comment.PostComment
import com.petbulance.domain.model.feature.community.comment.SearchPostCommentListRes
import com.petbulance.domain.model.feature.community.comment.UpdatePostCommentReq

interface CommentRepository {
    suspend fun updatePostComment(
        commentId: Long,
        request: UpdatePostCommentReq
    ): Result<PostComment>

    suspend fun deletePostComment(
        commentId: Long
    ): Result<DelComment>

    suspend fun searchPostCommentList(
        keyword: String,
        searchScope: String,
        lastCommentId: Long?,
        pageSize: Int,
        category: List<String>?,
        boardId: Long?
    ): Result<SearchPostCommentListRes>

    suspend fun getMyCommentList(
        keyword: String?,
        lastCommentId: Long?,
        pageSize: Int
    ): Result<MyCommentList>
}