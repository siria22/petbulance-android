package com.petbulance.presentation.screen.feature.home

import androidx.lifecycle.SavedStateHandle
import com.petbulance.domain.model.feature.community.post.PostSummary
import com.petbulance.domain.model.feature.home.HomeBanner
import com.petbulance.domain.model.feature.home.HomeScreenReview
import com.petbulance.domain.usecase.feature.community.GetHotArticleUseCase
import com.petbulance.domain.usecase.feature.home.GetHomeBannersUseCase
import com.petbulance.domain.usecase.feature.hospital.review.GetRecentReviewsUseCase
import com.petbulance.domain.utils.zip
import com.petbulance.presentation.utils.BaseViewModel
import com.petbulance.presentation.utils.SectionLoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getRecentReviewsUseCase: GetRecentReviewsUseCase,
    private val getHotArticleUseCase: GetHotArticleUseCase,
    private val getHomeBannersUseCase: GetHomeBannersUseCase
) : BaseViewModel() {

    private val _eventFlow = MutableSharedFlow<HomeEvent>()
    val eventFlow: SharedFlow<HomeEvent> = _eventFlow

    private val _reviewState = MutableStateFlow<SectionLoadState>(SectionLoadState.Init)
    val reviewState: StateFlow<SectionLoadState> = _reviewState

    private val _bannerState = MutableStateFlow<SectionLoadState>(SectionLoadState.Init)
    val bannerState: StateFlow<SectionLoadState> = _bannerState

    private val _hotArticleState = MutableStateFlow<SectionLoadState>(SectionLoadState.Init)
    val hotArticleState: StateFlow<SectionLoadState> = _hotArticleState

    private val _recentReviews = MutableStateFlow<List<HomeScreenReview>>(emptyList())
    val recentReviews: StateFlow<List<HomeScreenReview>> = _recentReviews

    private val _hotArticles = MutableStateFlow<List<PostSummary>>(emptyList())
    val hotArticles: StateFlow<List<PostSummary>> = _hotArticles

    private val _homeBanners = MutableStateFlow<List<HomeBanner>>(emptyList())
    val homeBanners: StateFlow<List<HomeBanner>> = _homeBanners

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.RetryReviews -> {
                launch { getRecentReviews() }
            }

            is HomeIntent.RetryBanners -> {
                launch { getHomeBanners() }
            }

            is HomeIntent.RetryHotArticles -> {
                launch { getHotArticle() }
            }
        }
    }

    init {
        observeErrorEvent(eventFlow)

        launch {
            zip(
                { getRecentReviews() },
                { getHotArticle() }
            )
            getHomeBanners()
        }
    }

    private suspend fun <T> loadSectionData(
        stateFlow: MutableStateFlow<SectionLoadState>,
        dataFlow: MutableStateFlow<T>,
        errorMessage: String,
        loader: suspend () -> T
    ) {
        stateFlow.value = SectionLoadState.Loading
        runCatching {
            loader()
        }.onSuccess { result ->
            dataFlow.value = result
            stateFlow.value = SectionLoadState.Success
        }.onFailure {
            stateFlow.value = SectionLoadState.Error(errorMessage)
        }
    }

    private suspend fun getHomeBanners() {
        loadSectionData(
            stateFlow = _bannerState,
            dataFlow = _homeBanners,
            errorMessage = "배너를 불러올 수 없습니다"
        ) {
            getHomeBannersUseCase().getOrThrow()
        }
    }

    private suspend fun getRecentReviews() {
        loadSectionData(
            stateFlow = _reviewState,
            dataFlow = _recentReviews,
            errorMessage = "최근 리뷰를 불러올 수 없습니다"
        ) {
            getRecentReviewsUseCase()
        }
    }

    private suspend fun getHotArticle() {
        loadSectionData(
            stateFlow = _hotArticleState,
            dataFlow = _hotArticles,
            errorMessage = "인기 게시글을 불러올 수 없습니다"
        ) {
            getHotArticleUseCase()
        }
    }
}
