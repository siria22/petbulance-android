package com.petbulance.presentation.screen.feature.search.info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.petbulance.domain.model.feature.hospital.hospital.Hospital
import com.petbulance.domain.model.feature.hospital.hospital.OpenHour
import com.petbulance.domain.model.feature.hospital.review.HospitalReview
import com.petbulance.domain.model.feature.hospital.review.PagingReviewList
import com.petbulance.domain.model.type.ReviewSortType
import com.petbulance.domain.usecase.feature.hospital.hospital.GetHospitalCardUseCase
import com.petbulance.domain.usecase.feature.hospital.hospital.GetHospitalDetailUseCase
import com.petbulance.domain.usecase.feature.hospital.review.GetHospitalReviewsUseCase
import com.petbulance.domain.utils.LocationUtils
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

    private val _infoData = MutableStateFlow(HospitalInfoData.init)
    val infoData: StateFlow<HospitalInfoData> = _infoData

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
                if (_infoData.value.reviewUiData.sortBy != intent.sortType) {
                    _infoData.update { it.copy(reviewUiData = it.reviewUiData.copy(sortBy = intent.sortType)) }
                    checkCacheAndLoad()
                }
            }

            is HospitalInfoIntent.ToggleImageOnly -> {
                _infoData.update { it.copy(reviewUiData = it.reviewUiData.copy(onlyImage = intent.isChecked)) }
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
                    { fetchHospitalDetail() },
                    { fetchInitialReviews() }
                )
            }.onSuccess { (hospital, detail, reviewPaging) ->
                val patchedHospital =
                    if (hospital.openHours == "(정보 없음)" || hospital.openHours == null) {
                        val calculatedHours =
                            calculateCurrentOpenHours(detail.openHours, hospital.isOpenNow)
                        if (calculatedHours != null) {
                            hospital.copy(openHours = calculatedHours)
                        } else {
                            hospital
                        }
                    } else {
                        hospital
                    }

                _infoData.update {
                    it.copy(hospitalUiData = HospitalUiData(hospital = patchedHospital, hospitalDetail = detail))
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

    private fun calculateCurrentOpenHours(openHours: List<OpenHour>, isOpenNow: Boolean): String? {
        val today = java.time.LocalDate.now()
        val dayKey = DAY_OF_WEEK_MAP[today.dayOfWeek] ?: return null

        val todaySchedule = openHours.find { it.day == dayKey } ?: return null
        val hoursStr = if (todaySchedule.hours == "CLOSED") "휴무" else todaySchedule.hours

        return if (isOpenNow) {
            if (hoursStr.contains("-")) {
                val parts = hoursStr.split("-")
                if (parts.size == 2) {
                    "${parts[1]}에 영업 종료"
                } else {
                    hoursStr
                }
            } else {
                hoursStr
            }
        } else {
            hoursStr
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
            onlyImageReview = _infoData.value.reviewUiData.onlyImage,
            cursorId = null,
            cursorRating = null,
            cursorLikeCount = null,
            sortBy = _infoData.value.reviewUiData.sortBy
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
            onlyImageReview = _infoData.value.reviewUiData.onlyImage,
            cursorId = currentCursorId,
            cursorRating = currentCursorRating,
            cursorLikeCount = currentCursorLikeCount,
            sortBy = _infoData.value.reviewUiData.sortBy
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

        _infoData.update {
            val currentReviews = it.reviewUiData.reviews
            it.copy(
                reviewUiData = it.reviewUiData.copy(
                    reviews = if (isAppend) currentReviews + reviewPaging.items else reviewPaging.items
                )
            )
        }
    }

    private fun updateCache(data: PagingReviewList<HospitalReview>) {
        reviewCache[getCurrentCacheKey()] = data
    }

    private fun getCurrentCacheKey() =
        Pair(_infoData.value.reviewUiData.sortBy, _infoData.value.reviewUiData.onlyImage)

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
        val card = getHospitalCardUseCase(hospitalId = hospitalId)
        val distance = LocationUtils.calculateDistance(lat, lng, card.lat, card.lng)
        return card.toHospital().copy(distanceMeters = distance)
    }

    private suspend fun fetchHospitalDetail() = getHospitalDetailUseCase(hospitalId = hospitalId)

    private suspend fun fetchInitialReviews() = getHospitalReviewsUseCase(
        hospitalId = hospitalId,
        onlyImageReview = _infoData.value.reviewUiData.onlyImage,
        cursorId = null,
        cursorRating = null,
        cursorLikeCount = null,
        sortBy = _infoData.value.reviewUiData.sortBy
    ).getOrThrow()

    companion object {
        private const val INVALID_ID = -1L
        private val DAY_OF_WEEK_MAP = mapOf(
            java.time.DayOfWeek.MONDAY to "MON",
            java.time.DayOfWeek.TUESDAY to "TUE",
            java.time.DayOfWeek.WEDNESDAY to "WED",
            java.time.DayOfWeek.THURSDAY to "THU",
            java.time.DayOfWeek.FRIDAY to "FRI",
            java.time.DayOfWeek.SATURDAY to "SAT",
            java.time.DayOfWeek.SUNDAY to "SUN"
        )
    }
}