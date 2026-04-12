package com.petbulance.domain.usecase.feature.community.post

import com.petbulance.domain.model.feature.community.post.PostCommentRes
import com.petbulance.domain.model.feature.community.post.param.CreateCommentParam
import com.petbulance.domain.repository.feature.community.PostRepository
import javax.inject.Inject

class CreateCommentUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(
        postId: Long,
        param: CreateCommentParam
    ): Result<PostCommentRes> {
        return postRepository.createComment(postId, param)
    }
}
