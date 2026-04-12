package com.petbulance.domain.usecase.feature.community.post

import com.petbulance.domain.model.feature.community.post.PostLike
import com.petbulance.domain.repository.feature.community.PostRepository
import javax.inject.Inject

class TogglePostLikeUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: Long, isCurrentlyLiked: Boolean): Result<PostLike> {
        return if (isCurrentlyLiked) {
            repository.unlikePost(postId)
        } else {
            repository.likePost(postId)
        }
    }
}
