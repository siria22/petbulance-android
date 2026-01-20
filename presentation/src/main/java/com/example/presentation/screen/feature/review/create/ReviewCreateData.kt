package com.example.presentation.screen.feature.review.create

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class ReceiptAnalysisResultUiModel(
    val hospitalName: String,
    val visitDate: String,
    val totalPrice: Long,
    val items: List<ReceiptItemUiModel>
) : Parcelable

@Serializable
@Parcelize
data class ReceiptItemUiModel(
    val name: String,
    val price: Int
) : Parcelable

