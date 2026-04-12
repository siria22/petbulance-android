package com.petbulance.domain.repository.feature.support

import com.petbulance.domain.model.feature.support.qna.DeleteQnaResult
import com.petbulance.domain.model.feature.support.qna.Qna
import com.petbulance.domain.model.feature.support.qna.QnaListResult
import com.petbulance.domain.model.feature.support.qna.QnaParam

interface QnaRepository {

    suspend fun getQnaList(lastQnaId: Long? = null, pageSize: Int = 10): Result<QnaListResult>

    suspend fun getQnaById(qnaId: Long): Result<Qna>

    suspend fun createQna(param: QnaParam): Result<Qna>

    suspend fun updateQna(qnaId: Long, param: QnaParam): Result<Qna>

    suspend fun deleteQna(qnaId: Long): Result<DeleteQnaResult>
}