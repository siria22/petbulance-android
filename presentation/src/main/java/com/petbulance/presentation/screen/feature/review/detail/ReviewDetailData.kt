package com.petbulance.presentation.screen.feature.review.detail

import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.model.type.AnimalSpecies

data class ReviewDetailData(
    val id: Long,
    val userNickname: String,
    val visitDate: String,
    val hospitalName: String,
    val animalType: AnimalCategory,
    val detailAnimalType: AnimalSpecies,
    val rating: Double,
    val price: Int,
    val isReceiptVerified: Boolean,
    val content: String,
    val images: List<String>,
    val likeCount: Int,
    val isLiked: Boolean
) {
    companion object {
        val empty = ReviewDetailData(
            id = 0L,
            userNickname = "",
            visitDate = "",
            hospitalName = "",
            animalType = AnimalCategory.ALL,
            detailAnimalType = AnimalSpecies.PARROT,
            rating = 0.0,
            price = 0,
            isReceiptVerified = false,
            content = "",
            images = emptyList(),
            likeCount = 0,
            isLiked = false
        )
    }
}