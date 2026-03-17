package com.petbulance.domain.usecase.feature.community.post

import com.petbulance.domain.model.feature.community.post.PagingPostSearchList
import com.petbulance.domain.repository.feature.community.PostRepository
import javax.inject.Inject

class GetPostSearchListUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(
        type: String? = null,
        topic: String? = null,
        sort: String = "latest",
        lastPostId: Long? = null,
        pageSize: Int = 20,
        searchKeyword: String,
        searchScope: String = "title_content"
    ): Result<PagingPostSearchList> {
        return repository.getPostSearchList(
            type = type,
            topic = topic,
            sort = sort,
            lastPostId = lastPostId,
            pageSize = pageSize,
            searchKeyword = searchKeyword,
            searchScope = searchScope
        )
    }
}
