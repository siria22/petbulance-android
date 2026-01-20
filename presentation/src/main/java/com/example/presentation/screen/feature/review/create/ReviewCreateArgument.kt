package com.example.presentation.screen.feature.review.create

import androidx.compose.runtime.Immutable
import com.example.domain.model.feature.hospital.review.HospitalInfo
import com.example.domain.model.feature.hospital.review.ReceiptAnalysisResult
import com.example.domain.model.feature.hospital.review.ReceiptItem
import com.example.domain.model.feature.hospital.review.ReviewRating
import kotlinx.coroutines.flow.SharedFlow

data class ReviewCreateArgument(
    val state: ReviewCreateState,
    val intent: (ReviewCreateIntent) -> Unit,
    val event: SharedFlow<ReviewCreateEvent>
)

@Immutable
data class ReviewCreateState(
    val currentStep: ReviewCreateStep = ReviewCreateStep.HOSPITAL_AND_RATING,
    val step1: Step1State = Step1State(),
    val step2: Step2State = Step2State(),
    val step3: Step3State = Step3State(),
    val isLoading: Boolean = false
)

enum class ReviewCreateStep {
    HOSPITAL_AND_RATING,  // 1단계: 병원 선택 및 평점
    ANIMAL_AND_TREATMENT, // 2단계: 동물 및 진료 정보
    REVIEW_CONTENT        // 3단계: 후기 내용 및 사진
}

@Immutable
data class Step1State(
    val hospitalInfo: HospitalInfo? = null,
    val ratings: ReviewRating = ReviewRating(0.0, 0.0, 0.0),
    val isReceiptVerified: Boolean = false
)

@Immutable
data class Step2State(
    val animalType: String = "",
    val detailAnimalType: String = "",
    val treatment: String = "",
    val visitDate: String = "",
    val price: Long = 0,
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

    // Step 1: Hospital & Rating
    data class OnHospitalSelected(val hospital: HospitalInfo) : ReviewCreateIntent
    data class OnRatingChanged(val rating: ReviewRating) : ReviewCreateIntent
    data class OnReceiptAnalyzed(val result: ReceiptAnalysisResult) : ReviewCreateIntent

    // Step 2: Animal & Treatment
    data class OnAnimalTypeChanged(val value: String) : ReviewCreateIntent
    data class OnDetailAnimalTypeChanged(val value: String) : ReviewCreateIntent
    data class OnTreatmentChanged(val value: String) : ReviewCreateIntent

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