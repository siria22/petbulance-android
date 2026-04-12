package com.petbulance.domain.model.feature.support.qna

data class QnaListResult(
    val qnaList: List<Qna>,
    val hasNext: Boolean
)
