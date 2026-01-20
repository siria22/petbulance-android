package com.example.domain.model.feature.hospital.review

data class ReceiptAnalysisResult(
    val hospitalId: Long,
    val hospitalName: String,
    val visitDate: String,
    val totalPrice: Long,
    val items: List<ReceiptItem>
)

data class ReceiptItem(
    val name: String,
    val price: Int
)