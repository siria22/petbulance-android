package com.petbulance.domain.usecase.feature.community.comment

import com.petbulance.domain.model.feature.community.comment.SearchPostCommentListRes
import com.petbulance.domain.repository.feature.community.CommentRepository
import javax.inject.Inject

class SearchPostCommentListUseCase @Inject constructor(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(
        searchKeyword: String,
        searchScope: String = "content",
        lastCommentId: Long? = null,
        pageSize: Int = 20,
        topic: String? = null,
        type: String? = null
    ): Result<SearchPostCommentListRes> {
        return repository.searchPostCommentList(
            searchKeyword = searchKeyword,
            searchScope = searchScope,
            lastCommentId = lastCommentId,
            pageSize = pageSize,
            topic = topic,
            type = type
        )
    }
}
