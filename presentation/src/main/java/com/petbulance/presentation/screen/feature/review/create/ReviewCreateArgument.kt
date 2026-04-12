package com.petbulance.presentation.screen.feature.review.create

import androidx.compose.runtime.Immutable
import com.petbulance.domain.model.feature.hospital.review.HospitalInfoForReview
import com.petbulance.domain.model.feature.hospital.review.ReceiptAnalysisResult
import com.petbulance.domain.model.feature.hospital.review.ReceiptItem
import com.petbulance.domain.model.feature.hospital.review.ReviewRating
import com.petbulance.domain.model.type.AnimalCategory
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
    val hospitalQuery: String = "",
    val hospitalCandidates: List<HospitalInfoForReview> = emptyList(),
    val hospitalInfoForReview: HospitalInfoForReview? = null,
    val totalPrice: String = "",
    val animalType: AnimalCategory = AnimalCategory.AVIAN,
    val detailAnimalType: String = "",
    val isReceiptVerified: Boolean = false
)

@Immutable
data class Step2State(
    val ratings: ReviewRating = ReviewRating(0.0, 0.0, 0.0),
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
    data object OnPreviousStep : ReviewCreateIntent

    // Step 1: Hospital, Cost & Treatment
    data class OnPriceChanged(val value: String) : ReviewCreateIntent
    data class OnReceiptAnalyzed(val result: ReceiptAnalysisResult) : ReviewCreateIntent
    data class OnHospitalQueryChanged(val query: String) : ReviewCreateIntent
    data class OnHospitalCandidateSelected(val hospital: HospitalInfoForReview) : ReviewCreateIntent
    data object OnHospitalClearClicked : ReviewCreateIntent

    // Step 2: Animal & Rating
    data class OnAnimalTypeChanged(val value: AnimalCategory) : ReviewCreateIntent
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
    data class OnSubmitSuccess(val reviewId: Long) : ReviewCreateEvent
}