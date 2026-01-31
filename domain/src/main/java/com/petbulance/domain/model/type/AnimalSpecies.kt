package com.petbulance.domain.model.type

/**
 * 동물 소분류
 */
enum class AnimalSpecies(val category: AnimalCategory, val korean: String) {
    // --- 소형 포유류 ---
    HAMSTER(AnimalCategory.SMALLMAMMALS, "햄스터"),
    GUINEAPIG(AnimalCategory.SMALLMAMMALS, "기니피그"),
    CHINCHILLA(AnimalCategory.SMALLMAMMALS, "친칠라"),
    RABBIT(AnimalCategory.SMALLMAMMALS, "토끼"),
    HEDGEHOG(AnimalCategory.SMALLMAMMALS, "고슴도치"),
    FERRET(AnimalCategory.SMALLMAMMALS, "페럿"),
    SUGAR_GLIDER(AnimalCategory.SMALLMAMMALS, "슈가글라이더"),
    PRAIRIE_DOG(AnimalCategory.SMALLMAMMALS, "프레리도그"),
    FLYING_SQUIRREL(AnimalCategory.SMALLMAMMALS, "하늘다람쥐"),
    OTHER_SMALL_MAMMALS(AnimalCategory.SMALLMAMMALS, "기타 소동물"),

    // --- 조류 ---
    PARROT(AnimalCategory.AVIAN, "앵무새"),
    FINCH_TYPES(AnimalCategory.AVIAN, "핀치류"),
    OTHER_BIRDS(AnimalCategory.AVIAN, "기타 조류"),

    // --- 파충류 ---
    GECKO(AnimalCategory.REPTILE, "게코"),
    OTHER_LIZARDS(AnimalCategory.REPTILE, "기타 도마뱀"),
    SNAKE(AnimalCategory.REPTILE, "뱀"),
    TURTLE(AnimalCategory.REPTILE, "거북이"),
    OTHER_REPTILES(AnimalCategory.REPTILE, "기타 파충류"),

    // --- 양서류 ---
    FROG(AnimalCategory.AMPHIBIAN, "개구리"),
    AXOLOTL(AnimalCategory.AMPHIBIAN, "우파루파"),
    SALAMANDER(AnimalCategory.AMPHIBIAN, "도롱뇽"),
    OTHER_AMPHIBIANS(AnimalCategory.AMPHIBIAN, "기타 양서류"),

    // --- 어류 ---
    ORNAMENTAL_FISH(AnimalCategory.FISH, "관상어");

    companion object {
        fun fromString(value: String?): AnimalSpecies {
            return AnimalSpecies.entries.find { it.name.equals(value, ignoreCase = true) }
                ?: OTHER_SMALL_MAMMALS
//                ?: throw IllegalArgumentException("Invalid AnimalSpecies: $value")
        }
    }
}

