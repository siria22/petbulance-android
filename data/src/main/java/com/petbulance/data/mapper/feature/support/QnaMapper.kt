package com.petbulance.data.mapper.feature.support

import com.petbulance.data.datasource.remote.network.feature.support.qna.dto.CreateQnaReqDto
import com.petbulance.data.datasource.remote.network.feature.support.qna.dto.CreateQnaResDto
import com.petbulance.data.datasource.remote.network.feature.support.qna.dto.DeleteQnaResDto
import com.petbulance.data.datasource.remote.network.feature.support.qna.dto.UpdateQnaReqDto
import com.petbulance.data.datasource.remote.network.feature.support.qna.dto.UpdateQnaResDto
import com.petbulance.domain.model.feature.support.qna.DeleteQnaResult
import com.petbulance.domain.model.feature.support.qna.Qna
import com.petbulance.domain.model.feature.support.qna.QnaParam

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
