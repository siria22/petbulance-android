package com.petbulance.domain.usecase.feature.community.comment

import com.petbulance.domain.model.feature.community.comment.PostComment
import com.petbulance.domain.model.feature.community.comment.UpdatePostCommentReq
import com.petbulance.domain.repository.feature.community.CommentRepository
import javax.inject.Inject

class UpdateCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(commentId: Long, request: UpdatePostCommentReq): Result<PostComment> {
        return commentRepository.updatePostComment(commentId, request)
    }
}
