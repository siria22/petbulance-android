package com.petbulance.presentation.screen.feature.search.info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.petbulance.domain.model.feature.hospital.hospital.Hospital
import com.petbulance.domain.model.feature.hospital.review.HospitalReview
import com.petbulance.domain.model.feature.hospital.review.PagingReviewList
import com.petbulance.domain.model.type.ReviewSortType
import com.petbulance.domain.usecase.feature.hospital.hospital.GetHospitalCardUseCase
import com.petbulance.domain.usecase.feature.hospital.hospital.GetHospitalDetailUseCase
import com.petbulance.domain.usecase.feature.hospital.review.GetHospitalReviewsUseCase
import com.petbulance.domain.utils.zip
import com.petbulance.presentation.utils.BaseViewModel
import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.nav.ScreenDestinations
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

    private val hospitalId: Long =
        savedStateHandle.get<Long>(ScreenDestinations.Search.HospitalInfo.ARG_ID)
            ?: INVALID_ID

    private val _dataState = MutableStateFlow<HospitalInfoDataState>(HospitalInfoDataState.Init)
    val dataState: StateFlow<HospitalInfoDataState> = _dataState

    private val _hospitalUiData = MutableStateFlow(HospitalUiData())
    val hospitalUiData: StateFlow<HospitalUiData> = _hospitalUiData

    private val _reviewUiData = MutableStateFlow(ReviewUiData())
    val reviewUiData: StateFlow<ReviewUiData> = _reviewUiData

    private val _eventFlow = MutableSharedFlow<HospitalInfoEvent>()
    val eventFlow: SharedFlow<HospitalInfoEvent> = _eventFlow

    // Paging State
    private var currentCursorId: Long? = null
    private var currentCursorRating: Double? = null
    private var currentCursorLikeCount: Long? = null
    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false

    // Cache (Key: SortType + OnlyImage)
    private var reviewLoadJob: Job? = null
    private val reviewCache =
        mutableMapOf<Pair<ReviewSortType, Boolean>, PagingReviewList<HospitalReview>>()

    init {
        observeErrorEvent(eventFlow)
        if (hospitalId == INVALID_ID) {
            viewModelScope.launch {
                emitError(IllegalArgumentException("잘못된 병원 정보입니다."))
            }
        }
    }

    fun onIntent(intent: HospitalInfoIntent) {
        if (hospitalId == INVALID_ID) return

        when (intent) {
            is HospitalInfoIntent.LoadData -> loadData(intent.lat, intent.lng)
            is HospitalInfoIntent.LoadMoreReviews -> {
                launch { loadMoreReviews() }
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
            resetPagingState()

            runCatching {
                zip(
                    { fetchHospital(lat, lng) },
                    { fetchHospitalDetail(lat, lng) },
                    { fetchInitialReviews() }
                )
            }.onSuccess { (hospital, detail, reviewPaging) ->
                _hospitalUiData.update {
                    it.copy(hospital = hospital, hospitalDetail = detail)
                }

                applyReviewData(reviewPaging, isAppend = false)
                updateCache(reviewPaging)

                _dataState.value = HospitalInfoDataState.Init
            }.onFailure { exception ->
                emitError(exception)
                _dataState.value = HospitalInfoDataState.Init
            }
        }
    }

    private fun checkCacheAndLoad() {
        val currentKey = getCurrentCacheKey()
        val cachedData = reviewCache[currentKey]

        if (cachedData != null) {
            applyReviewData(cachedData, isAppend = false)
        } else {
            loadReviewsWithCancellation()
        }
    }

    private suspend fun reloadReviewsOnly() {
        _dataState.value = HospitalInfoDataState.OnProgress
        resetPagingState()

        getHospitalReviewsUseCase(
            hospitalId = hospitalId,
            onlyImageReview = _reviewUiData.value.onlyImage,
            cursorId = null,
            cursorRating = null,
            cursorLikeCount = null,
            sortBy = _reviewUiData.value.sortBy
        ).onSuccess { reviewPaging ->
            updateCache(reviewPaging)
            applyReviewData(reviewPaging, isAppend = false)
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
            applyReviewData(pagingResult, isAppend = true)
        }.onFailure { exception ->
            emitError(exception)
        }
        isLoading = false
    }

    private fun applyReviewData(reviewPaging: PagingReviewList<HospitalReview>, isAppend: Boolean) {
        // 다음 요청을 위한 커서 갱신 로직 (Critical Fix)
        isLastPage = !reviewPaging.hasNext
        currentCursorId = reviewPaging.nextCursorId

        // 마지막 아이템을 찾아 커서 정보 갱신
        val lastItem = reviewPaging.items.lastOrNull()
        if (lastItem != null) {
            currentCursorRating = lastItem.rating
            currentCursorLikeCount = lastItem.likeCount.toLong()
        }

        _reviewUiData.update {
            it.copy(
                reviews = if (isAppend) it.reviews + reviewPaging.items else reviewPaging.items
            )
        }
    }

    private fun updateCache(data: PagingReviewList<HospitalReview>) {
        reviewCache[getCurrentCacheKey()] = data
    }

    private fun getCurrentCacheKey() = Pair(_reviewUiData.value.sortBy, _reviewUiData.value.onlyImage)

    private fun resetPagingState() {
        currentCursorId = null
        currentCursorRating = null
        currentCursorLikeCount = null
        isLastPage = false
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

    private suspend fun fetchHospitalDetail(lat: Double?, lng: Double?) =
        getHospitalDetailUseCase(
            hospitalId = hospitalId,
            userLat = lat ?: 0.0,
            userLng = lng ?: 0.0
        )

    private suspend fun fetchInitialReviews() = getHospitalReviewsUseCase(
        hospitalId = hospitalId,
        onlyImageReview = _reviewUiData.value.onlyImage,
        cursorId = null,
        cursorRating = null,
        cursorLikeCount = null,
        sortBy = _reviewUiData.value.sortBy
    ).getOrThrow()

    companion object {
        private const val INVALID_ID = -1L
    }
}