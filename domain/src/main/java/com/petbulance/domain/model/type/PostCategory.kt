package com.petbulance.domain.model.type

enum class PostCategory(val korean: String) {
    HEALTH("건강/질병"),
    SUPPLIES("용품/사료"),
    DAILY("일상/자랑"),
    TRADE("중고거래");


    companion object {
        fun fromString(value: String?): PostCategory {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: DAILY
        }

        fun toKorean(value: PostCategory): String {
            return entries.find { it.name.equals(value.name, ignoreCase = true) }?.name
                ?: "(알 수 없음)"
        }
    }
}