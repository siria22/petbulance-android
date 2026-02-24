package com.petbulance.presentation.screen.feature.home

import com.petbulance.presentation.utils.SectionLoadState
import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class HomeArgument(
    val intent: (HomeIntent) -> Unit,
    val reviewState: SectionLoadState,
    val bannerState: SectionLoadState,
    val hotArticleState: SectionLoadState,
    val dataState: HomeDataState,
    val screenState: HomeScreenState,
    val event: SharedFlow<HomeEvent>
)

sealed class HomeDataState {
    data object Init : HomeDataState()
    data object OnProgress : HomeDataState()
}

sealed class HomeScreenState {
    data object Init: HomeScreenState()
}

sealed class HomeIntent {
    data class SomeIntentWithParams(val param: String) : HomeIntent()
    data object SomeIntentWithoutParams : HomeIntent()
    data object RetryReviews : HomeIntent()
    data object RetryBanners : HomeIntent()
    data object RetryHotArticles : HomeIntent()
}

sealed class HomeEvent {
    sealed class DataFetch : HomeEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?,
            override val displayType: ErrorDisplayType = ErrorDisplayType.Common
        ) : DataFetch(), ErrorEvent
    }
    
    sealed class Section : HomeEvent() {
        data class ReviewsError(val message: String) : Section()
        data class BannersError(val message: String) : Section()
        data class HotArticlesError(val message: String) : Section()
    }
}