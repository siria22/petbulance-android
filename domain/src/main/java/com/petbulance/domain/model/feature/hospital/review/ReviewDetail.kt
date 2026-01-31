package com.petbulance.domain.model.feature.hospital.review

import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.model.type.AnimalSpecies

data class ReviewDetail(
    val userNickname: String,
    val receiptCheck: Boolean,
    val id: Long,
    val hospitalImage: String?,
    val hospitalId: Long,
    val hospitalName: String,
    val treatmentService: String,
    val animalType: AnimalCategory,
    val detailAnimalType: AnimalSpecies,
    val reviewContent: String,
    val facilityRating: Double,
    val expertiseRating: Double,
    val kindnessRating: Double,
    val createDate: String,
    val totalPrice: Int,
    val likeCount: Int,
    val liked: Boolean,
    val visitDate: String,
    val images: List<String>,
    val userProfileImage: String? = null,
    val viewCount: Int = 0,
    val isAuthor: Boolean = false,
) {
    companion object {
        fun stub() = ReviewDetail(
            userNickname = "user",
            receiptCheck = true,
            id = 1L,
            hospitalImage = null,
            hospitalId = 1L,
            hospitalName = "hospital",
            treatmentService = "treatment",
            animalType = AnimalCategory.fromString("BIRD"),
            detailAnimalType = AnimalSpecies.fromString("PARROT"),
            reviewContent = "review",
            facilityRating = 3.0,
            expertiseRating = 4.0,
            kindnessRating = 5.0,
            createDate = "2020-01-01",
            totalPrice = 10000,
            likeCount = 1,
            liked = false,
            visitDate = "2020-01-01",
            images = emptyList()
        )
    }
}