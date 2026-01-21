package com.petbulance.data.repository.feature.support.qna

import com.petbulance.domain.model.feature.support.qna.DeleteQnaResult
import com.petbulance.domain.model.feature.support.qna.Qna
import com.petbulance.domain.model.feature.support.qna.QnaParam
import com.petbulance.domain.repository.feature.support.QnaRepository
import javax.inject.Inject

class MockQnaRepository @Inject constructor() : QnaRepository {
    override suspend fun createQna(param: QnaParam): Result<Qna> =
        Result.success(
            Qna(
                id = 1L,
                title = param.title,
                content = param.content,
                date = "2024-01-21",
            )
        )

    override suspend fun updateQna(qnaId: Long, param: QnaParam): Result<Qna> =
        Result.success(
            Qna(
                id = qnaId,
                title = param.title,
                content = param.content,
                date = "2024-01-21",
            )
        )

    override suspend fun deleteQna(qnaId: Long): Result<DeleteQnaResult> =
        Result.success(
            DeleteQnaResult(
                id = 1L,
                message = "deleted"
            )
        )
}