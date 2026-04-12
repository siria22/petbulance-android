package com.petbulance.domain.usecase.feature.community

import com.petbulance.domain.model.feature.community.post.PostSummary
import com.petbulance.domain.usecase.feature.community.post.GetPostListUseCase
import javax.inject.Inject

class GetHotArticleUseCase @Inject constructor(
    private val getPostListUseCase: GetPostListUseCase
) {
    suspend operator fun invoke(): List<PostSummary> {
        return getPostListUseCase(
            sort = SORT_POPULAR,
            pageSize = PAGE_SIZE
        ).getOrThrow().items
    }

    companion object {
        private const val SORT_POPULAR = "popular"
        private const val PAGE_SIZE = 4
    }
}
