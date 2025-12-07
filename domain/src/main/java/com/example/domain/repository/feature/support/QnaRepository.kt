package com.example.domain.repository.feature.support

import com.example.domain.model.feature.support.qna.DeleteQnaResult
import com.example.domain.model.feature.support.qna.Qna
import com.example.domain.model.feature.support.qna.QnaParam

interface QnaRepository {

    suspend fun createQna(param: QnaParam): Result<Qna>

    suspend fun updateQna(qnaId: Long, param: QnaParam): Result<Qna>

    suspend fun deleteQna(qnaId: Long): Result<DeleteQnaResult>
}