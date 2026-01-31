package com.petbulance.presentation.screen.feature.home

import androidx.lifecycle.SavedStateHandle
import com.petbulance.domain.model.feature.community.post.PostDetail
import com.petbulance.domain.model.feature.home.HomeBanner
import com.petbulance.domain.model.feature.home.HomeScreenReview
import com.petbulance.domain.usecase.feature.community.GetHotArticleUseCase
import com.petbulance.domain.usecase.feature.home.GetHomeBannersUseCase
import com.petbulance.domain.usecase.feature.hospital.review.GetRecentReviewsUseCase
import com.petbulance.domain.utils.zip
import com.petbulance.presentation.utils.BaseViewModel
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

    private val _dataState = MutableStateFlow<HomeDataState>(HomeDataState.Init)
    val dataState: StateFlow<HomeDataState> = _dataState

    private val _screenState = MutableStateFlow<HomeScreenState>(HomeScreenState.Init)
    val screenState: StateFlow<HomeScreenState> = _screenState

    private val _eventFlow = MutableSharedFlow<HomeEvent>()
    val eventFlow: SharedFlow<HomeEvent> = _eventFlow

    private val _recentReviews = MutableStateFlow<List<HomeScreenReview>>(emptyList())
    val recentReviews: StateFlow<List<HomeScreenReview>> = _recentReviews

    private val _hotArticles = MutableStateFlow<List<PostDetail>?>(null)
    val hotArticles: StateFlow<List<PostDetail>?> = _hotArticles

    private val _homeBanners = MutableStateFlow<List<HomeBanner>>(emptyList())
    val homeBanners: StateFlow<List<HomeBanner>> = _homeBanners

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.SomeIntentWithoutParams -> {
                //do sth
            }

            is HomeIntent.SomeIntentWithParams -> {
                //do sth(intent.params)
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

    private suspend fun getHomeBanners() {
        getHomeBannersUseCase()
            .onSuccess { result ->
                _homeBanners.value = result
            }
            .onFailure {
            }
    }

    private suspend fun getRecentReviews() {
        _dataState.value = HomeDataState.OnProgress
        runCatching {
            getRecentReviewsUseCase()
        }.onSuccess { result ->
            _recentReviews.value = result
        }.onFailure { ex ->
            _eventFlow.emit(
                HomeEvent.DataFetch.Error(
                    userMessage = "최근 리뷰를 가져오는데 실패했습니다.",
                    exceptionMessage = ex.message
                )
            )
        }
        _dataState.value = HomeDataState.Init
    }

    private suspend fun getHotArticle() {
        _dataState.value = HomeDataState.OnProgress
        runCatching {
            getHotArticleUseCase()
        }.onSuccess { result ->
            _hotArticles.value = result
        }.onFailure { ex ->
            _eventFlow.emit(
                HomeEvent.DataFetch.Error(
                    userMessage = "최근 게시글을 가져오는데 실패했습니다.",
                    exceptionMessage = ex.message
                )
            )
        }
        _dataState.value = HomeDataState.Init
    }
}
