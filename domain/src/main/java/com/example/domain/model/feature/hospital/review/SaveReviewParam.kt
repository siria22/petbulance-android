package com.example.domain.model.feature.hospital.review

data class SaveReviewParam(
    val hospitalId: Long,
    val rating: ReviewRating,
    val price: Long,
    val animalType: String,
    val detailAnimalType: String,
    val receiptItems: List<ReceiptItem>,
    val visitDate: String,
    val comment: String,
    val isReceipt: Boolean,
    val title: String? = null,
    val images: List<ReviewImageParam>? = null
)

data class ReviewImageParam(
    val filename: String,
    val contentType: String
)