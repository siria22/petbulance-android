package com.example.data.repository.feature.support.qna

import com.example.data.datasource.remote.network.feature.support.qna.QnaApi
import com.example.data.datasource.remote.network.feature.support.qna.dto.CreateQnaResDto
import com.example.data.datasource.remote.network.feature.support.qna.dto.DeleteQnaResDto
import com.example.data.datasource.remote.network.feature.support.qna.dto.UpdateQnaResDto
import com.example.data.datasource.remote.network.common.safeApiCall
import com.example.data.mapper.feature.support.toCreateQnaDto
import com.example.data.mapper.feature.support.toDomain
import com.example.data.mapper.feature.support.toUpdateDto
import com.example.domain.model.feature.support.qna.DeleteQnaResult
import com.example.domain.model.feature.support.qna.Qna
import com.example.domain.model.feature.support.qna.QnaParam
import com.example.domain.repository.feature.support.QnaRepository
import javax.inject.Inject

class QnaRepositoryImpl @Inject constructor(
    private val api: QnaApi
) : QnaRepository {

    override suspend fun createQna(param: QnaParam): Result<Qna> {
        return safeApiCall<CreateQnaResDto>(path = "/qna") {
            api.createQna(param.toCreateQnaDto())
        }.map { it: CreateQnaResDto -> it.toDomain() }
    }

    override suspend fun updateQna(
        qnaId: Long,
        param: QnaParam
    ): Result<Qna> {
        return safeApiCall<UpdateQnaResDto>(path = "/qna/$qnaId") {
            api.updateQna(qnaId, param.toUpdateDto())
        }.map { it: UpdateQnaResDto -> it.toDomain() }
    }

    override suspend fun deleteQna(qnaId: Long): Result<DeleteQnaResult> {
        return safeApiCall<DeleteQnaResDto>(path = "/qna/$qnaId") {
            api.deleteQna(qnaId)
        }.map { it.toDomain() }
    }
}