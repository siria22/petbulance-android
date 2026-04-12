package com.petbulance.domain.model.feature.hospital.review

import com.petbulance.domain.model.type.AnimalCategory

data class SaveReviewParam(
    val hospitalId: Long,
    val rating: ReviewRating,
    val price: Long,
    val animalType: AnimalCategory,
    val detailAnimalType: String,
    val receiptItems: List<ReceiptItem>,
    val visitDate: String?,
    val comment: String,
    val isReceipt: Boolean,
    val title: String? = null,
    val imageCount: Int = 0
)

data class ReviewImageParam(
    val filename: String,
    val contentType: String,
    val content: String,
    val isReceipt: Boolean
)