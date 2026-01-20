package com.example.domain.model.type

enum class ReportType(val korean: String) {
    POST("게시글"),
    COMMENT("댓글"),
    REVIEW("리뷰"),
    OTHERS("기타");

    companion object {
        fun fromString(value: String?): ReportType {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: OTHERS
        }

        fun toKorean(value: ReportType): String {
            return entries.find { it.name.equals(value.name, ignoreCase = true) }?.name
                ?: "(알 수 없음)"
        }
    }
}