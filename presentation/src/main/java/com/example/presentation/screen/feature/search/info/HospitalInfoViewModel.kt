package com.example.presentation.screen.feature.search.info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.domain.model.feature.hospital.hospital.Hospital
import com.example.domain.model.feature.hospital.review.HospitalReview
import com.example.domain.model.feature.hospital.review.PagingReviewList
import com.example.domain.model.type.ReviewSortType
import com.example.domain.usecase.feature.hospital.hospital.GetHospitalCardUseCase
import com.example.domain.usecase.feature.hospital.hospital.GetHospitalDetailUseCase
import com.example.domain.usecase.feature.hospital.review.GetHospitalReviewsUseCase
import com.example.domain.utils.zip
import com.example.presentation.utils.BaseViewModel
import com.example.presentation.utils.error.ErrorDisplayType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class HospitalInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getHospitalCardUseCase: GetHospitalCardUseCase,
    private val getHospitalDetailUseCase: GetHospitalDetailUseCase,
    private val getHospitalReviewsUseCase: GetHospitalReviewsUseCase
) : BaseViewModel() {

    private val hospitalId: Long = savedStateHandle.get<Long>("hospitalId") ?: -1L

    private val _dataState = MutableStateFlow<HospitalInfoDataState>(HospitalInfoDataState.Init)
    val dataState: StateFlow<HospitalInfoDataState> = _dataState

    private val _hospitalUiData = MutableStateFlow(HospitalUiData())
    val hospitalUiData: StateFlow<HospitalUiData> = _hospitalUiData

    private val _reviewUiData = MutableStateFlow(ReviewUiData())
    val reviewUiData: StateFlow<ReviewUiData> = _reviewUiData

    private val _eventFlow = MutableSharedFlow<HospitalInfoEvent>()
    val eventFlow: SharedFlow<HospitalInfoEvent> = _eventFlow

    // Paging 관련 상태
    private var currentCursorId: Long? = null
    private var currentCursorRating: Double? = null
    private var currentCursorLikeCount: Long? = null
    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false

    // Cache
    private var reviewLoadJob: Job? = null
    private val reviewCache =
        mutableMapOf<Pair<ReviewSortType, Boolean>, PagingReviewList<HospitalReview>>()

    init {
        observeErrorEvent(eventFlow)
    }

    fun onIntent(intent: HospitalInfoIntent) {
        when (intent) {
            is HospitalInfoIntent.LoadData -> loadData(intent.lat, intent.lng)
            is HospitalInfoIntent.LoadMoreReviews -> {
                launch {
                    loadMoreReviews()
                }
            }

            is HospitalInfoIntent.ChangeReviewSort -> {
                if (_reviewUiData.value.sortBy != intent.sortType) {
                    _reviewUiData.update { it.copy(sortBy = intent.sortType) }
                    checkCacheAndLoad()
                }
            }

            is HospitalInfoIntent.ToggleImageOnly -> {
                _reviewUiData.update { it.copy(onlyImage = intent.isChecked) }
                checkCacheAndLoad()
            }
        }
    }

    private fun loadData(lat: Double?, lng: Double?) {
        launch {
            _dataState.value = HospitalInfoDataState.OnProgress
            runCatching {
                zip(
                    { fetchHospital(lat, lng) },
                    { fetchHospitalDetail() },
                    { fetchInitialReviews() }
                )
            }.onSuccess { (hospital, detail, reviewPaging) ->
                _hospitalUiData.update {
                    it.copy(hospital = hospital, hospitalDetail = detail)
                }
                applyReviewData(reviewPaging)

                val key = Pair(_reviewUiData.value.sortBy, _reviewUiData.value.onlyImage)
                reviewCache[key] = reviewPaging

                _dataState.value = HospitalInfoDataState.Init
            }.onFailure { exception ->
                emitError(exception)
                _dataState.value = HospitalInfoDataState.Init
            }
        }
    }

    private fun checkCacheAndLoad() {
        val currentSort = _reviewUiData.value.sortBy
        val currentOnlyImage = _reviewUiData.value.onlyImage

        val cacheKey = Pair(currentSort, currentOnlyImage)
        val cachedData = reviewCache[cacheKey]

        if (cachedData != null) {
            applyReviewData(cachedData)
        } else {
            loadReviewsWithCancellation()
        }
    }

    private suspend fun reloadReviewsOnly() {
        _dataState.value = HospitalInfoDataState.OnProgress
        getHospitalReviewsUseCase(
            hospitalId = hospitalId,
            onlyImageReview = _reviewUiData.value.onlyImage,
            cursorId = null,
            cursorRating = null,
            cursorLikeCount = null,
            sortBy = _reviewUiData.value.sortBy
        ).onSuccess { reviewPaging ->
            val currentSort = _reviewUiData.value.sortBy
            val currentOnlyImage = _reviewUiData.value.onlyImage

            reviewCache[Pair(currentSort, currentOnlyImage)] = reviewPaging

            applyReviewData(reviewPaging)
            _dataState.value = HospitalInfoDataState.Init
        }.onFailure { exception ->
            if (exception !is CancellationException) {
                emitError(exception)
                _dataState.value = HospitalInfoDataState.Init
            }
        }
    }

    private fun loadReviewsWithCancellation() {
        reviewLoadJob?.cancel()
        reviewLoadJob = viewModelScope.launch {
            reloadReviewsOnly()
        }
    }

    private fun applyReviewData(reviewPaging: PagingReviewList<HospitalReview>) {
        currentCursorId = reviewPaging.nextCursorId
        isLastPage = !reviewPaging.hasNext

        _reviewUiData.update {
            it.copy(reviews = reviewPaging.items)
        }
    }

    private suspend fun loadMoreReviews() {
        if (isLoading || isLastPage) return
        isLoading = true

        getHospitalReviewsUseCase(
            hospitalId = hospitalId,
            onlyImageReview = _reviewUiData.value.onlyImage,
            cursorId = currentCursorId,
            cursorRating = currentCursorRating,
            cursorLikeCount = currentCursorLikeCount,
            sortBy = _reviewUiData.value.sortBy
        ).onSuccess { pagingResult ->
            currentCursorId = pagingResult.nextCursorId
            isLastPage = !pagingResult.hasNext

            _reviewUiData.update {
                it.copy(
                    reviews = it.reviews + pagingResult.items
                )
            }
        }.onFailure { exception ->
            emitError(exception)
        }
        isLoading = false
    }

    private suspend fun emitError(exception: Throwable) {
        _eventFlow.emit(
            HospitalInfoEvent.DataFetch.Error(
                displayType = ErrorDisplayType.Common,
                userMessage = "데이터를 불러오는데 실패했습니다.",
                exceptionMessage = exception.message
            )
        )
    }

    private suspend fun fetchHospital(lat: Double?, lng: Double?): Hospital {
        val card = getHospitalCardUseCase(
            hospitalId = hospitalId,
            userLat = lat ?: 0.0,
            userLng = lng ?: 0.0
        )
        return card.toHospital()
    }

    private suspend fun fetchHospitalDetail() = getHospitalDetailUseCase(hospitalId = hospitalId)

    private suspend fun fetchInitialReviews() = getHospitalReviewsUseCase(
        hospitalId = hospitalId,
        onlyImageReview = _reviewUiData.value.onlyImage,
        cursorId = null,
        cursorRating = null,
        cursorLikeCount = null,
        sortBy = _reviewUiData.value.sortBy
    ).getOrThrow()
}