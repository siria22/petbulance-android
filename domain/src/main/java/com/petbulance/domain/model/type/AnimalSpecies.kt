package com.petbulance.domain.model.type

enum class AnimalSpecies(val category: AnimalCategory) {
    // --- 소형 포유류 ---
    HAMSTER(AnimalCategory.SMALL_MAMMAL),
    GUINEAPIG(AnimalCategory.SMALL_MAMMAL),
    CHINCHILLA(AnimalCategory.SMALL_MAMMAL),
    RABBIT(AnimalCategory.SMALL_MAMMAL),
    HEDGEHOG(AnimalCategory.SMALL_MAMMAL),
    FERRET(AnimalCategory.SMALL_MAMMAL),
    SUGAR_GLIDER(AnimalCategory.SMALL_MAMMAL),
    PRAIRIE_DOG(AnimalCategory.SMALL_MAMMAL),
    FLYING_SQUIRREL(AnimalCategory.SMALL_MAMMAL),
    OTHER_SMALL_MAMMALS(AnimalCategory.SMALL_MAMMAL),

    // --- 조류 ---
    PARROT(AnimalCategory.BIRD),
    FINCH_TYPES(AnimalCategory.BIRD),
    OTHER_BIRDS(AnimalCategory.BIRD),

    // --- 파충류 ---
    GECKO(AnimalCategory.REPTILE),
    OTHER_LIZARDS(AnimalCategory.REPTILE),
    SNAKE(AnimalCategory.REPTILE),
    TURTLE(AnimalCategory.REPTILE),
    OTHER_REPTILES(AnimalCategory.REPTILE),

    // --- 양서류 ---
    FROG(AnimalCategory.AMPHIBIAN),
    AXOLOTL(AnimalCategory.AMPHIBIAN),
    SALAMANDER(AnimalCategory.AMPHIBIAN),
    OTHER_AMPHIBIANS(AnimalCategory.AMPHIBIAN),

    // --- 어류 ---
    ORNAMENTAL_FISH(AnimalCategory.FISH)
}

fun AnimalSpecies.toKorean(): String {
    return when (this) {
        AnimalSpecies.HAMSTER -> "햄스터"
        AnimalSpecies.GUINEAPIG -> "기니피그"
        AnimalSpecies.CHINCHILLA -> "친칠라"
        AnimalSpecies.RABBIT -> "토끼"
        AnimalSpecies.HEDGEHOG -> "고슴도치"
        AnimalSpecies.FERRET -> "페럿"
        AnimalSpecies.SUGAR_GLIDER -> "슈가글라이더"
        AnimalSpecies.PRAIRIE_DOG -> "프레리도그"
        AnimalSpecies.FLYING_SQUIRREL -> "하늘다람쥐"
        AnimalSpecies.OTHER_SMALL_MAMMALS -> "기타소동물"

        AnimalSpecies.PARROT -> "앵무새"
        AnimalSpecies.FINCH_TYPES -> "핀치류"
        AnimalSpecies.OTHER_BIRDS -> "기타 조류"

        AnimalSpecies.GECKO -> "게코"
        AnimalSpecies.OTHER_LIZARDS -> "기타 도마뱀"
        AnimalSpecies.SNAKE -> "뱀"
        AnimalSpecies.TURTLE -> "거북이"
        AnimalSpecies.OTHER_REPTILES -> "기타 파충류"

        AnimalSpecies.FROG -> "개구리"
        AnimalSpecies.AXOLOTL -> "우파루파"
        AnimalSpecies.SALAMANDER -> "도롱뇽"
        AnimalSpecies.OTHER_AMPHIBIANS -> "기타 양서류"

        AnimalSpecies.ORNAMENTAL_FISH -> "관상어"
    }
}