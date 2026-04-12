package com.petbulance.presentation.screen.feature.search.info

import com.petbulance.domain.model.type.ReviewSortType
import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class HospitalInfoArgument(
    val state: HospitalInfoDataState,
    val intent: (HospitalInfoIntent) -> Unit,
    val event: SharedFlow<HospitalInfoEvent>
)

sealed class HospitalInfoDataState {
    data object Init : HospitalInfoDataState()
    data object OnProgress : HospitalInfoDataState()
}

sealed class HospitalInfoIntent {
    data class LoadData(val lat: Double?, val lng: Double?) : HospitalInfoIntent()
    data object LoadMoreReviews : HospitalInfoIntent()
    data class ChangeReviewSort(val sortType: ReviewSortType) : HospitalInfoIntent()
    data class ToggleImageOnly(val isChecked: Boolean) : HospitalInfoIntent()
}

sealed class HospitalInfoEvent {
    sealed class DataFetch : HospitalInfoEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }
}