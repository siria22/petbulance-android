package com.petbulance.domain.model.type

enum class AppTheme(val korean: String) {
    DEVICE("기기 설정"),
    DAYLIGHT("일반"),
    DARK("다크 모드");

    companion object {

        fun fromString(value: String?): AppTheme {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: DEVICE
        }

        fun toKorean(value: AppTheme): String {
            return entries.find { it.name.equals(value.name, ignoreCase = true) }?.korean ?: "(알 수 없음)"
        }
    }
}
