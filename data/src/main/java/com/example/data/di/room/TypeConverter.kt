package com.example.data.di.room

import androidx.room.TypeConverter
import java.time.LocalDate
import kotlin.let

object LocalDateConverter {

    /**
     * String(DB) -> LocalDate?(앱)
     *
     * @param value 데이터베이스에 저장된 YYYY-MM-DD 형식의 문자열 또는 null.
     * @return 파싱된 LocalDate 객체 또는 null.
     */
    @TypeConverter
    fun fromStringToLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    /**
     * LocalDate?(앱) -> String(DB)
     *
     * @param date 변환할 LocalDate 객체 또는 null.
     * @return YYYY-MM-DD 형식의 문자열 또는 null.
     */
    @TypeConverter
    fun fromLocalDateToString(date: LocalDate?): String? {
        return date?.toString()
    }
}