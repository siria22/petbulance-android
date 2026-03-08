package com.petbulance.domain.model.feature.support.qna

enum class QnaStatus(val value: String, val korean: String) {
    ANSWER_WAITING("ANSWER_WAITING", "답변대기"),
    ANSWER_COMPLETED("ANSWER_COMPLETED", "답변완료");

    companion object {
        fun fromValue(value: String): QnaStatus {
            return entries.find { it.value == value }
                ?: throw IllegalArgumentException("Unknown QnaStatus: $value")
        }
    }
}
