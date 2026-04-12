package com.petbulance.domain.usecase.feature.community.post

import com.petbulance.domain.model.feature.community.post.DeletedPost
import com.petbulance.domain.repository.feature.community.PostRepository
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: Long): Result<DeletedPost> {
        return repository.deletePost(postId)
    }
}
