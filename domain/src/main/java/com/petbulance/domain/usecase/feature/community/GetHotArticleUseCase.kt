package com.petbulance.domain.usecase.feature.community

import com.petbulance.domain.model.feature.community.post.PostSummary
import com.petbulance.domain.usecase.feature.community.post.GetPostListUseCase
import javax.inject.Inject

class GetHotArticleUseCase @Inject constructor(
    private val getPostListUseCase: GetPostListUseCase
) {
    suspend operator fun invoke(): List<PostSummary> {
        return getPostListUseCase(
            sort = "popular",
            pageSize = 4
        ).getOrNull()?.items ?: emptyList()
    }
}
