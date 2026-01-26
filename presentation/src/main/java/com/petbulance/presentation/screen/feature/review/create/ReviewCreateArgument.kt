package com.petbulance.presentation.screen.feature.review.create

import androidx.compose.runtime.Immutable
import com.petbulance.domain.model.feature.hospital.review.HospitalInfo
import com.petbulance.domain.model.feature.hospital.review.ReceiptAnalysisResult
import com.petbulance.domain.model.feature.hospital.review.ReceiptItem
import com.petbulance.domain.model.feature.hospital.review.ReviewRating
import kotlinx.coroutines.flow.SharedFlow

data class ReviewCreateArgument(
    val state: ReviewCreateState,
    val intent: (ReviewCreateIntent) -> Unit,
    val event: SharedFlow<ReviewCreateEvent>
)

@Immutable
data class ReviewCreateState(
    val currentStep: ReviewCreateStep = ReviewCreateStep.HOSPITAL_AND_COST,
    val step1: Step1State = Step1State(),
    val step2: Step2State = Step2State(),
    val step3: Step3State = Step3State(),
    val isLoading: Boolean = false,
    val showValidationError: Boolean = false
)

enum class ReviewCreateStep {
    HOSPITAL_AND_COST,
    ANIMAL_AND_RATING,
    REVIEW_CONTENT
}

@Immutable
data class Step1State(
    val hospitalInfo: HospitalInfo? = null,
    val totalPrice: String = "",
    val treatments: List<String> = listOf(""),
    val isReceiptVerified: Boolean = false
)

@Immutable
data class Step2State(
    val animalType: String = "",
    val detailAnimalType: String = "",
    val ratings: ReviewRating = ReviewRating(0.0, 0.0, 0.0), // Step1에서 이동
    val visitDate: String = "",
    val receiptItems: List<ReceiptItem> = emptyList()
)

@Immutable
data class Step3State(
    val content: String = "",
    val images: List<String> = emptyList()
)

sealed interface ReviewCreateIntent {
    // Navigation & Common
    data object OnBackClicked : ReviewCreateIntent
    data object OnNextClicked : ReviewCreateIntent
    data object OnCloseClicked : ReviewCreateIntent

    // Step 1: Hospital, Cost & Treatment
    data class OnHospitalSelected(val hospitalName: String) : ReviewCreateIntent
    data class OnPriceChanged(val value: String) : ReviewCreateIntent
    data class OnTreatmentChanged(val index: Int, val value: String) : ReviewCreateIntent
    data object OnTreatmentAdded : ReviewCreateIntent
    data class OnTreatmentRemoved(val index: Int) : ReviewCreateIntent
    data class OnReceiptAnalyzed(val result: ReceiptAnalysisResult) : ReviewCreateIntent

    // Step 2: Animal & Rating
    data class OnAnimalTypeChanged(val value: String) : ReviewCreateIntent
    data class OnDetailAnimalTypeChanged(val value: String) : ReviewCreateIntent
    data class OnRatingChanged(val rating: ReviewRating) : ReviewCreateIntent

    // Step 3: Content & Image
    data class OnContentChanged(val value: String) : ReviewCreateIntent
    data class OnImagesChanged(val images: List<String>) : ReviewCreateIntent
    data object OnSubmitClicked : ReviewCreateIntent
}

sealed interface ReviewCreateEvent {
    data object ShowExitDialog : ReviewCreateEvent
    data class ShowToast(val message: String) : ReviewCreateEvent
    data object NavigateBack : ReviewCreateEvent
    data object NavigateToHome : ReviewCreateEvent
}