package com.petbulance.domain.utils

fun dowInKorean(day:String) = when(day) {
    "MON" -> "월요일"
    "TUE" -> "화요일"
    "WED" -> "수요일"
    "THU" -> "목요일"
    "FRI" -> "금요일"
    "SAT" -> "토요일"
    "SUN" -> "일요일"
    else -> "공휴일"
}