package com.petbulance.domain.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

/**
 * LocalDateTime 객체를 ISO_LOCAL_DATE_TIME 형식의 문자열로 변환합니다.
 * 예: "2023-12-07T15:30:00"
 */
fun LocalDateTime.toIsoString(): String {
    return this.format(formatter)
}

/**
 * ISO_LOCAL_DATE_TIME 형식의 문자열을 LocalDateTime 객체로 변환합니다.
 * 변환에 실패할 경우 null을 반환합니다.
 */
fun String.toLocalDateTime(): LocalDateTime? {
    return try {
        LocalDateTime.parse(this, formatter)
    } catch (e: DateTimeParseException) {
        println("Failed to parse LocalDateTime: $this - ${e.message}")
        null
    }
}