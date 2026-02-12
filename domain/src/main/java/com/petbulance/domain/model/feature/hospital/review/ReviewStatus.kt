package com.petbulance.domain.model.feature.hospital.review

enum class ReviewStatus(val korean: String) {
    REGISTERED("등록완료"),
    HIDDEN("반려"),
    UNDER_REVIEW("검수중");

    companion object {
        fun fromString(value: String?): ReviewStatus {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: REGISTERED
        }

        fun toKorean(value: ReviewStatus): String {
            return entries.find { it.name.equals(value.name, ignoreCase = true) }?.name
                ?: "(알 수 없음)"
        }
    }
}