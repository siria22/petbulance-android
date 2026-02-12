package com.petbulance.domain.model.type

enum class NoticeStatusType(val korean: String) {
    EVENT("이벤트"),
    ADVERTISING("광고"),
    NOTICE("공지");

    companion object {
        fun fromString(value: String?): NoticeStatusType {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: NOTICE
        }

        fun toKorean(value: NoticeStatusType): String {
            return entries.find { it.name.equals(value.name, ignoreCase = true) }?.name
                ?: "(알 수 없음)"
        }
    }
}