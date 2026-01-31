package com.petbulance.presentation.screen.feature.review.edit

import androidx.compose.runtime.Immutable
import com.petbulance.domain.model.feature.hospital.review.ReceiptItem
import com.petbulance.domain.model.feature.hospital.review.ReviewRating
import com.petbulance.domain.model.type.AnimalCategory
import kotlinx.coroutines.flow.SharedFlow

data class ReviewEditArgument(
    val state: ReviewEditState,
    val intent: (ReviewEditIntent) -> Unit,
    val event: SharedFlow<ReviewEditEvent>
)

@Immutable
data class ReviewEditState(
    val isLoading: Boolean = false,
    val reviewId: Long = -1L,
    val hospitalId: Long = 0L,
    val hospitalName: String = "",
    val totalCost: String = "",
    val animalType: AnimalCategory = AnimalCategory.AVIAN,
    val detailAnimalType: String = "",
    val ratings: ReviewRating = ReviewRating(0.0, 0.0, 0.0),
    val existingImages: List<String> = emptyList(),
    val newImages: List<String> = emptyList(),
    val content: String = "",
    val visitDate: String = "",
    val receiptItems: List<ReceiptItem> = emptyList(),
    val isReceiptVerified: Boolean = false,
    val showValidationError: Boolean = false
)

sealed interface ReviewEditIntent {
    data object OnBackClicked : ReviewEditIntent
    data class OnHospitalNameChanged(val value: String) : ReviewEditIntent
    data class OnTotalCostChanged(val value: String) : ReviewEditIntent
    data class OnAnimalTypeChanged(val value: AnimalCategory) : ReviewEditIntent
    data class OnDetailAnimalTypeChanged(val value: String) : ReviewEditIntent
    data class OnRatingChanged(val value: ReviewRating) : ReviewEditIntent
    data class OnContentChanged(val value: String) : ReviewEditIntent
    data class OnNewImagesChanged(val images: List<String>) : ReviewEditIntent
    data class OnRemoveImage(val index: Int) : ReviewEditIntent
    data object OnSubmitClicked : ReviewEditIntent


}

sealed interface ReviewEditEvent {
    data object ShowExitDialog : ReviewEditEvent
    data class ShowToast(val message: String) : ReviewEditEvent
    data object NavigateBack : ReviewEditEvent
}