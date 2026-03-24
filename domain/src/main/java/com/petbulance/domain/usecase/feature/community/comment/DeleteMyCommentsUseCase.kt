package com.petbulance.domain.usecase.feature.community.comment

import com.petbulance.domain.repository.feature.community.CommentRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class DeleteMyCommentsUseCase @Inject constructor(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(commentIds: List<Long>): Result<Unit> {
        if (commentIds.isEmpty()) return Result.success(Unit)

        val results = coroutineScope {
            commentIds.map { commentId ->
                async { repository.deletePostComment(commentId) }
            }.awaitAll()
        }

        val firstFailure = results.firstOrNull { it.isFailure }
        return firstFailure?.let {
            Result.failure(it.exceptionOrNull() ?: Exception("댓글 삭제에 실패했습니다."))
        } ?: Result.success(Unit)
    }
}
