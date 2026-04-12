package com.petbulance.domain.usecase.feature.community.comment

import com.petbulance.domain.model.feature.community.comment.MyCommentList
import com.petbulance.domain.repository.feature.community.CommentRepository
import javax.inject.Inject

class GetMyCommentListUseCase @Inject constructor(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(
        keyword: String? = null,
        lastCommentId: Long? = null,
        pageSize: Int = 20
    ): Result<MyCommentList> {
        return repository.getMyCommentList(
            searchKeyword = keyword,
            lastCommentId = lastCommentId,
            pageSize = pageSize
        )
    }
}
