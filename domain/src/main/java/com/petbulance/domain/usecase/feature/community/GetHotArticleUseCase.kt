package com.petbulance.domain.usecase.feature.community

import com.petbulance.domain.model.feature.community.post.PostDetail
import com.petbulance.domain.repository.feature.community.PostRepository
import javax.inject.Inject

class GetHotArticleUseCase @Inject constructor(
    private val repository: PostRepository
) {
    // TODO : Get Hot Article by some criteria
    suspend operator fun invoke(): List<PostDetail> {
        return listOf(PostDetail.stub)
    }
}