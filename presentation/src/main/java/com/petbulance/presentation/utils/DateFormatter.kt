package com.petbulance.presentation.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * ISO 8601 형식의 날짜 문자열을 "yyyy.MM.dd" 형식으로 변환
 * 예: "2026-03-06T12:27:00.880175" -> "2025.11.10"
 */
fun String.formatReviewDate(): String {
    return try {
        // ISO 8601 형식 파싱 (T와 밀리초 포함)
        val dateTime = LocalDateTime.parse(this, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        dateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
    } catch (e: DateTimeParseException) {
        // 파싱 실패 시 원본 문자열 반환
        this
    } catch (e: Exception) {
        // 기타 예외 발생 시 원본 문자열 반환
        this
    }
}
