package com.example.domain.model.feature.hospital.review

data class HospitalReview(
    val id: Long,
    val isReceiptVerified: Boolean,
    val treatment: String,
    val animalType: String,
    val detailAnimalType: String,
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
        val stub = HospitalReview(
            id = 1,
            isReceiptVerified = true,
            treatment = "Treatment",
            animalType = "Animal Type",
            detailAnimalType = "소형포유류",
            content = "햄스터가 설사해서 병원 갔는데, 대기는 30분 정도 했어요. 원장님이 꼼꼼하게 봐주시고 설명도 잘해주셔서 안심이 됐습니다." +
                    "약먹고 금방 나았어요. 비용이 전혀 아깝지 않았습니다. 추천합니다!",
            rating = 5.0,
            date = "Date",
            likeCount = 10,
            isLiked = true,
            imageUrls = listOf("https://example.com/image1.jpg", "https://example.com/image2.jpg"),
            author = "작성자",
            price = 50000
        )
    }
}