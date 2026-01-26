package com.petbulance.domain.model.type

enum class AnimalCategory(val korean: String) {
    ALL("전체"),
    SMALL_MAMMAL("소형 포유류"),
    BIRD("조류"),
    REPTILE("파충류"),
    AMPHIBIAN("양서류"),
    FISH("어류");

    companion object {
        fun fromString(value: String?): AnimalCategory {
            return entries.find { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Invalid AnimalCategory: $value")
        }

        fun toKorean(value: AnimalCategory): String {
            return entries.find { it.name.equals(value.name, ignoreCase = true) }?.name
                ?: "(알 수 없음)"
        }
    }
}
