package com.petbulance.domain.usecase.feature.community.comment

import com.petbulance.domain.model.feature.community.comment.DelComment
import com.petbulance.domain.repository.feature.community.CommentRepository
import javax.inject.Inject

class DeleteCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(commentId: Long): Result<DelComment> {
        return commentRepository.deletePostComment(commentId)
    }
}
