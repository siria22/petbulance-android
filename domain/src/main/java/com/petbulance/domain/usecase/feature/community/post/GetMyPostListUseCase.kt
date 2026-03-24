package com.petbulance.domain.usecase.feature.community.post

import com.petbulance.domain.model.feature.community.post.PagingMyPostList
import com.petbulance.domain.repository.feature.community.PostRepository
import javax.inject.Inject

class GetMyPostListUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(
        keyword: String? = null,
        lastPostId: Long? = null,
        pageSize: Int = 20
    ): Result<PagingMyPostList> {
        return repository.getMyPostList(
            keyword = keyword,
            lastPostId = lastPostId,
            pageSize = pageSize
        )
    }
}
