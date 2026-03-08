package com.petbulance.data.mapper.feature.support

import com.petbulance.data.datasource.remote.network.feature.support.qna.dto.CreateQnaReqDto
import com.petbulance.data.datasource.remote.network.feature.support.qna.dto.CreateQnaResDto
import com.petbulance.data.datasource.remote.network.feature.support.qna.dto.DeleteQnaResDto
import com.petbulance.data.datasource.remote.network.feature.support.qna.dto.PagingQnaListResDto
import com.petbulance.data.datasource.remote.network.feature.support.qna.dto.QnaAnswerDto
import com.petbulance.data.datasource.remote.network.feature.support.qna.dto.QnaDetailResDto
import com.petbulance.data.datasource.remote.network.feature.support.qna.dto.QnaListItemResDto
import com.petbulance.data.datasource.remote.network.feature.support.qna.dto.UpdateQnaReqDto
import com.petbulance.data.datasource.remote.network.feature.support.qna.dto.UpdateQnaResDto
import com.petbulance.domain.model.feature.support.qna.DeleteQnaResult
import com.petbulance.domain.model.feature.support.qna.Qna
import com.petbulance.domain.model.feature.support.qna.QnaAnswer
import com.petbulance.domain.model.feature.support.qna.QnaListResult
import com.petbulance.domain.model.feature.support.qna.QnaParam
import com.petbulance.domain.model.feature.support.qna.QnaStatus

fun QnaParam.toCreateQnaDto() = CreateQnaReqDto(title, content)

fun CreateQnaResDto.toDomain() = Qna(
    id = qnaId,
    title = title,
    content = content,
    date = createdAt,
    status = QnaStatus.ANSWER_WAITING,
    answer = null
)

fun QnaParam.toUpdateDto() = UpdateQnaReqDto(title, content)

fun UpdateQnaResDto.toDomain() = Qna(
    id = qnaId,
    title = title,
    content = content,
    date = updatedAt,
    status = QnaStatus.ANSWER_WAITING,
    answer = null
)

fun DeleteQnaResDto.toDomain() = DeleteQnaResult(
    id = qnaId,
    message = message
)

fun PagingQnaListResDto.toDomain() = QnaListResult(
    qnaList = content.map { it.toDomain() },
    hasNext = hasNext
)

fun QnaListItemResDto.toDomain() = Qna(
    id = qnaId,
    title = title,
    content = content,
    date = createdAt,
    status = QnaStatus.fromValue(status),
    answer = null
)

fun QnaDetailResDto.toDomain() = Qna(
    id = qnaId,
    title = title,
    content = content,
    date = createdAt,
    status = QnaStatus.fromValue(status),
    answer = answer?.toDomain()
)

fun QnaAnswerDto.toDomain() = QnaAnswer(
    content = content,
    answeredAt = answeredAt
)
