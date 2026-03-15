package com.petbulance.domain.usecase.feature.community.post

import com.petbulance.domain.model.feature.community.post.PagingPostList
import com.petbulance.domain.repository.feature.community.PostRepository
import javax.inject.Inject

class GetPostListUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(
        type: String? = null,
        topic: String? = null,
        sort: String = "latest",
        lastPostId: Long? = null,
        pageSize: Int = 10
    ): Result<PagingPostList> {
        return repository.getPostList(
            type = type,
            topic = topic,
            sort = sort,
            lastPostId = lastPostId,
            pageSize = pageSize
        )
    }
}
