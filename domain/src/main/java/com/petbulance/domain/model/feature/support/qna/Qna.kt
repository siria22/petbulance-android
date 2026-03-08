package com.petbulance.domain.model.feature.support.qna

data class Qna(
    val id: Long,
    val title: String,
    val content: String,
    val date: String,
    val status: QnaStatus,
    val answer: QnaAnswer? = null
)