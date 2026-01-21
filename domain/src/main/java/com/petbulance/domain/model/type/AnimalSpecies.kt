package com.petbulance.domain.model.type

enum class AnimalSpecies(val category: AnimalCategory) {
    // --- 소형 포유류 (SMALL_MAMMAL) ---
    HAMSTER(AnimalCategory.SMALL_MAMMAL),       // 햄스터
    RABBIT(AnimalCategory.SMALL_MAMMAL),        // 토끼
    GUINEA_PIG(AnimalCategory.SMALL_MAMMAL),    // 기니피그
    SUGAR_GLIDER(AnimalCategory.SMALL_MAMMAL),  // 슈가글라이더
    HEDGEHOG(AnimalCategory.SMALL_MAMMAL),      // 고슴도치
    CHINCHILLA(AnimalCategory.SMALL_MAMMAL),    // 친칠라
    FERRET(AnimalCategory.SMALL_MAMMAL),        // 페럿
    PRAIRIE_DOG(AnimalCategory.SMALL_MAMMAL),   // 프레리도그
    FLYING_SQUIRREL(AnimalCategory.SMALL_MAMMAL), // 하늘다람쥐
    ETC_SMALL_ANIMAL(AnimalCategory.SMALL_MAMMAL), // 기타소동물

    // --- 조류 (BIRD) ---
    PARROT(AnimalCategory.BIRD),        // 앵무새
    FINCH(AnimalCategory.BIRD),         // 핀치류
    ETC_BIRD(AnimalCategory.BIRD),      // 기타 조류

    // --- 파충류 (REPTILE) ---
    GECKO(AnimalCategory.REPTILE),        // 게코
    ETC_LIZARD(AnimalCategory.REPTILE),   // 기타 도마뱀
    TURTLE(AnimalCategory.REPTILE),       // 거북이
    ETC_REPTILE(AnimalCategory.REPTILE),  // 기타 파충류

    // --- 양서류 (AMPHIBIAN) ---
    FROG(AnimalCategory.AMPHIBIAN),         // 개구리
    AXOLOTL(AnimalCategory.AMPHIBIAN),      // 우파루파
    SALAMANDER(AnimalCategory.AMPHIBIAN),   // 도롱뇽
    ETC_AMPHIBIAN(AnimalCategory.AMPHIBIAN),  // 기타 양서류

    // --- 어류 (FISH) ---
    ORNAMENTAL_FISH(AnimalCategory.FISH)  // 관상어
}

fun AnimalSpecies.toKorean(): String {
    return when (this) {
        AnimalSpecies.HAMSTER -> "햄스터"
        AnimalSpecies.RABBIT -> "토끼"
        AnimalSpecies.GUINEA_PIG -> "기니피그"
        AnimalSpecies.SUGAR_GLIDER -> "슈가글라이더"
        AnimalSpecies.HEDGEHOG -> "고슴도치"
        AnimalSpecies.CHINCHILLA -> "친칠라"
        AnimalSpecies.FERRET -> "페럿"
        AnimalSpecies.PRAIRIE_DOG -> "프레리도그"
        AnimalSpecies.FLYING_SQUIRREL -> "하늘다람쥐"
        AnimalSpecies.ETC_SMALL_ANIMAL -> "기타소동물"
        AnimalSpecies.PARROT -> "앵무새"
        AnimalSpecies.FINCH -> "핀치류"
        AnimalSpecies.ETC_BIRD -> "기타 조류"
        AnimalSpecies.GECKO -> "게코"
        AnimalSpecies.ETC_LIZARD -> "기타 도마뱀"
        AnimalSpecies.TURTLE -> "거북이"
        AnimalSpecies.ETC_REPTILE -> "기타 파충류"
        AnimalSpecies.FROG -> "개구리"
        AnimalSpecies.AXOLOTL -> "우파루파"
        AnimalSpecies.SALAMANDER -> "도롱뇽"
        AnimalSpecies.ETC_AMPHIBIAN -> "기타 양서류"
        AnimalSpecies.ORNAMENTAL_FISH -> "관상어"
    }
}