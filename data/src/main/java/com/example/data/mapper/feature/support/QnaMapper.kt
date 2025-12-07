package com.example.data.mapper.feature.support

import com.example.data.datasource.remote.network.feature.support.qna.dto.CreateQnaReqDto
import com.example.data.datasource.remote.network.feature.support.qna.dto.CreateQnaResDto
import com.example.data.datasource.remote.network.feature.support.qna.dto.DeleteQnaResDto
import com.example.data.datasource.remote.network.feature.support.qna.dto.UpdateQnaReqDto
import com.example.data.datasource.remote.network.feature.support.qna.dto.UpdateQnaResDto
import com.example.domain.model.feature.support.qna.DeleteQnaResult
import com.example.domain.model.feature.support.qna.Qna
import com.example.domain.model.feature.support.qna.QnaParam

fun QnaParam.toCreateQnaDto() = CreateQnaReqDto(title, content)

fun CreateQnaResDto.toDomain() = Qna(
    qnaId,
    title,
    content,
    createdAt
)

fun QnaParam.toUpdateDto() = UpdateQnaReqDto(title, content)

fun UpdateQnaResDto.toDomain() = Qna(
    qnaId,
    title,
    content,
    updatedAt
)

fun DeleteQnaResDto.toDomain() = DeleteQnaResult(
    id = qnaId,
    message = message
)
