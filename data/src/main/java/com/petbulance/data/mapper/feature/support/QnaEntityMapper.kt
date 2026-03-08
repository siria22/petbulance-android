package com.petbulance.data.mapper.feature.support

import com.petbulance.data.datasource.local.entity.QnaEntity
import com.petbulance.domain.model.feature.support.qna.Qna
import com.petbulance.domain.model.feature.support.qna.QnaAnswer
import com.petbulance.domain.model.feature.support.qna.QnaStatus

fun QnaEntity.toDomain() = Qna(
    id = id,
    title = title,
    content = content,
    date = date,
    status = QnaStatus.fromValue(status),
    answer = if (answerContent != null && answerDate != null) {
        QnaAnswer(content = answerContent, answeredAt = answerDate)
    } else null
)

fun Qna.toEntity() = QnaEntity(
    id = id,
    title = title,
    content = content,
    date = date,
    status = status.value,
    answerContent = answer?.content,
    answerDate = answer?.answeredAt,
    cachedAt = System.currentTimeMillis()
)
