package com.petbulance.domain.model.feature.hospital.review

import com.petbulance.domain.model.type.AnimalCategory

data class ModifyReviewParam(
    val reviewId: Long,
    val hospitalId: Long,
    val title: String,
    val rating: ReviewRating,
    val price: Long,
    val animalType: AnimalCategory,
    val detailAnimalType: String,
    val receiptItems: List<ReceiptItem>,
    val visitDate: String,
    val comment: String,
    val isReceipt: Boolean,
    val images: List<ReviewImageParam> = emptyList()
)