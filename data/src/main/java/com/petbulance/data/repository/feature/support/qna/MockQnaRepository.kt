package com.petbulance.data.repository.feature.support.qna

import com.petbulance.domain.model.feature.support.qna.DeleteQnaResult
import com.petbulance.domain.model.feature.support.qna.Qna
import com.petbulance.domain.model.feature.support.qna.QnaListResult
import com.petbulance.domain.model.feature.support.qna.QnaParam
import com.petbulance.domain.model.feature.support.qna.QnaStatus
import com.petbulance.domain.repository.feature.support.QnaRepository
import javax.inject.Inject

class MockQnaRepository @Inject constructor() : QnaRepository {
    override suspend fun getQnaList(lastQnaId: Long?, pageSize: Int): Result<QnaListResult> =
        Result.success(
            QnaListResult(
                qnaList = listOf(
                    Qna(
                        id = 1L,
                        title = "Mock QnA 1",
                        content = "Mock content 1",
                        date = "2024-01-21",
                        status = QnaStatus.ANSWER_WAITING,
                        answer = null
                    ),
                    Qna(
                        id = 2L,
                        title = "Mock QnA 2",
                        content = "Mock content 2",
                        date = "2024-01-20",
                        status = QnaStatus.ANSWER_COMPLETED,
                        answer = null
                    )
                ),
                hasNext = false
            )
        )

    override suspend fun getQnaById(qnaId: Long): Result<Qna> =
        Result.success(
            Qna(
                id = qnaId,
                title = "Mock QnA",
                content = "Mock content",
                date = "2024-01-21",
                status = QnaStatus.ANSWER_WAITING,
                answer = null
            )
        )

    override suspend fun createQna(param: QnaParam): Result<Qna> =
        Result.success(
            Qna(
                id = 1L,
                title = param.title,
                content = param.content,
                date = "2024-01-21",
                status = QnaStatus.ANSWER_WAITING,
                answer = null
            )
        )

    override suspend fun updateQna(qnaId: Long, param: QnaParam): Result<Qna> =
        Result.success(
            Qna(
                id = qnaId,
                title = param.title,
                content = param.content,
                date = "2024-01-21",
                status = QnaStatus.ANSWER_WAITING,
                answer = null
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