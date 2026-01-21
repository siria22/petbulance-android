package com.petbulance.domain.model.type

interface BaseEnumType {
    fun fromString(value: String): BaseEnumType
    fun toKorean(): String
}