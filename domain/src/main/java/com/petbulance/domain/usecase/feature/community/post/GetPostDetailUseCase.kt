package com.petbulance.domain.usecase.feature.community.post

import com.petbulance.domain.model.feature.community.post.PostDetail
import com.petbulance.domain.repository.feature.community.PostRepository
import javax.inject.Inject

class GetPostDetailUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: Long): Result<PostDetail> {
        return repository.getPostDetail(postId)
    }
}
