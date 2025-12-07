package com.example.domain.model.feature.hospital.review

data class SaveReviewParam(
    val hospitalId: Long,
    val rating: ReviewRating,
    val price: Long,
    val animalType: String,
    val detailAnimalType: String,
    val treatment: String,
    val visitDate: String,
    val comment: String,
    val isReceipt: Boolean
)