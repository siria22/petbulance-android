package com.petbulance.domain.usecase.feature.community.post

import com.petbulance.domain.model.feature.community.post.PagingCommentList
import com.petbulance.domain.repository.feature.community.PostRepository
import javax.inject.Inject

class GetCommentListUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(
        postId: Long,
        lastParentCommentId: Long? = null,
        lastCommentId: Long? = null,
        pageSize: Int = 15
    ): Result<PagingCommentList> {
        return postRepository.getCommentList(
            postId = postId,
            lastParentCommentId = lastParentCommentId,
            lastCommentId = lastCommentId,
            pageSize = pageSize
        )
    }
}
