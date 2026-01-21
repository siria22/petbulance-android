package com.petbulance.domain.model.feature.hospital.review

data class ReviewSearchItem(
    val id: Long,
    val hospitalName: String,
    val content: String,
    val rating: Double,
    val treatment: String,
    val isReceiptVerified: Boolean,
    val animalType: String,
    val totalReviewCount: Int
)

fun ReviewSearchItem.toHospitalReview(): HospitalReview {
    return HospitalReview(
        id = this.id,
        isReceiptVerified = this.isReceiptVerified,
        treatment = this.treatment,
        animalType = this.animalType,
        detailAnimalType = this.animalType,
        content = this.content,
        rating = this.rating,
        date = "", // 검색 결과 API에서 날짜 미제공
        likeCount = 0, // 검색 결과 API에서 좋아요 수 미제공
        isLiked = false,
        imageUrls = emptyList(), // 검색 결과 API에서 이미지 미제공 (필요 시 API 수정 요청)
        author = "", // 검색 결과 API에서 작성자 미제공
        price = 0,
        hospitalName = this.hospitalName
    )
}