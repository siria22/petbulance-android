package com.petbulance.domain.model.feature.hospital.review

import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.model.type.AnimalSpecies

data class HospitalReview(
    val id: Long,
    val hospitalName: String,
    val isReceiptVerified: Boolean,
    val treatment: String,
    val animalType: AnimalCategory,
    val detailAnimalType: AnimalSpecies,
    val content: String,
    val rating: Double,
    val date: String,
    val likeCount: Int,
    val isLiked: Boolean,
    val imageUrls: List<String>,
    val author: String,
    val price: Int
) {
    companion object {
        fun stub() = HospitalReview(
            id = 1,
            isReceiptVerified = true,
            treatment = "Treatment",
            animalType = AnimalCategory.fromString("BIRD"),
            detailAnimalType = AnimalSpecies.fromString("PARROT"),
            content = "햄스터가 설사해서 병원 갔는데, 대기는 30분 정도 했어요. 원장님이 꼼꼼하게 봐주시고 설명도 잘해주셔서 안심이 됐습니다." +
                    "약먹고 금방 나았어요. 비용이 전혀 아깝지 않았습니다. 추천합니다!",
            rating = 5.0,
            date = "Date",
            likeCount = 10,
            isLiked = true,
            imageUrls = listOf("https://example.com/image1.jpg", "https://example.com/image2.jpg"),
            author = "작성자",
            price = 50000,
            hospitalName = "행복동물병원",
        )

        fun stubs() = listOf(
            HospitalReview(
                id = 1,
                isReceiptVerified = true,
                treatment = "Treatment",
                animalType = AnimalCategory.fromString("BIRD"),
                detailAnimalType = AnimalSpecies.fromString("PARROT"),
                content = "햄스터가 설사해서 병원 갔는데, 대기는 30분 정도 했어요. 원장님이 꼼꼼하게 봐주시고 설명도 잘해주셔서 안심이 됐습니다." +
                        "약먹고 금방 나았어요. 비용이 전혀 아깝지 않았습니다. 추천합니다!",
                rating = 5.0,
                date = "Date",
                likeCount = 10,
                isLiked = true,
                imageUrls = listOf("https://example.com/image1.jpg", "https://example.com/image2.jpg"),
                author = "작성자",
                price = 50000,
                hospitalName = "행복동물병원",
            ),
            HospitalReview(
                id = 2,
                hospitalName = "행복동물병원",
                isReceiptVerified = true,
                treatment = "예방접종",
                animalType = AnimalCategory.fromString("FISH"),
                detailAnimalType = AnimalSpecies.fromString("ORNAMENTAL_FISH"),
                content = "휴! 행복해지네요 ㅎㅎ",
                rating = 4.2,
                date = "2024-11-20",
                likeCount = 11,
                isLiked = false,
                imageUrls = listOf(),
                author = "내가 썼어요",
                price = 100_000
            )
        )
    }
}