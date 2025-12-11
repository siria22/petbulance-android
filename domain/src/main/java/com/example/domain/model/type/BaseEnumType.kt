package com.example.domain.model.type

interface BaseEnumType {
    fun fromString(value: String): BaseEnumType
    fun toKorean(): String
}