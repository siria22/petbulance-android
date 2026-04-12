package com.petbulance.domain.usecase.feature.community.post

import com.petbulance.domain.repository.feature.community.PostRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class DeleteMyPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postIds: List<Long>): Result<Unit> {
        if (postIds.isEmpty()) return Result.success(Unit)

        val results = coroutineScope {
            postIds.map { postId ->
                async { repository.deletePost(postId) }
            }.awaitAll()
        }

        val firstFailure = results.firstOrNull { it.isFailure }
        return firstFailure?.let {
            Result.failure(it.exceptionOrNull() ?: Exception("게시글 삭제에 실패했습니다."))
        } ?: Result.success(Unit)
    }
}
