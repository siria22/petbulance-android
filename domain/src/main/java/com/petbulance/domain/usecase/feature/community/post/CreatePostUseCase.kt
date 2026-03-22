package com.petbulance.domain.usecase.feature.community.post

import com.petbulance.domain.model.feature.community.post.Post
import com.petbulance.domain.model.feature.community.post.param.CreatePostParam
import com.petbulance.domain.repository.feature.community.PostRepository
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(param: CreatePostParam): Result<Post> {
        return repository.createPost(param)
    }
}
