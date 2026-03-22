package com.petbulance.domain.usecase.feature.community.post

import com.petbulance.domain.model.feature.community.post.Post
import com.petbulance.domain.model.feature.community.post.param.UpdatePostParam
import com.petbulance.domain.repository.feature.community.PostRepository
import javax.inject.Inject

class UpdatePostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: Long, param: UpdatePostParam): Result<Post> {
        return repository.updatePost(postId, param)
    }
}
