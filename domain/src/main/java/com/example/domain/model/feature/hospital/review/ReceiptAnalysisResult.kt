package com.example.domain.model.feature.hospital.review

// TODO : 명세 확인
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