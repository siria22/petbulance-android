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
        searchKeyword: String,
        searchScope: String,
        lastCommentId: Long?,
        pageSize: Int,
        topic: String?,
        type: String?
    ): Result<SearchPostCommentListRes>

    suspend fun getMyCommentList(
        searchKeyword: String?,
        lastCommentId: Long?,
        pageSize: Int
    ): Result<MyCommentList>
}