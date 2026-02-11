package com.petbulance.presentation.screen.feature.search.main

import android.location.Location
import com.petbulance.domain.model.feature.hospital.hospital.Hospital
import com.petbulance.domain.model.feature.hospital.hospital.MapBounds
import com.petbulance.domain.model.feature.hospital.recent.RecentSearchKeyword
import com.petbulance.domain.model.feature.hospital.recent.ViewedHospitalList
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.model.type.AnimalSpecies
import com.petbulance.domain.model.type.HospitalSortType
import com.petbulance.domain.model.type.Region
import com.petbulance.domain.usecase.feature.hospital.hospital.SearchHospitalsUseCase
import com.petbulance.domain.usecase.feature.hospital.recent.AddSearchKeywordUseCase
import com.petbulance.domain.usecase.feature.hospital.recent.AddViewedHospitalUseCase
import com.petbulance.domain.usecase.feature.hospital.recent.DeleteRecentSearchKeywordUseCase
import com.petbulance.domain.usecase.feature.hospital.recent.DeleteViewedHospitalUseCase
import com.petbulance.domain.usecase.feature.hospital.recent.GetRecentSearchKeywordUseCase
import com.petbulance.domain.usecase.feature.hospital.recent.GetViewedHospitalsUseCase
import com.petbulance.domain.usecase.feature.hospital.recent.SyncSearchHistoryUseCase
import com.petbulance.domain.utils.LocationUtils
import com.petbulance.presentation.screen.feature.search.main.views.search.HospitalSearchQueryUiModel
import com.petbulance.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class HospitalSearchViewModel @Inject constructor(
    private val searchHospitalsUseCase: SearchHospitalsUseCase,
    private val getRecentSearchKeywordUseCase: GetRecentSearchKeywordUseCase,
    private val addSearchKeywordUseCase: AddSearchKeywordUseCase,
    private val deleteRecentSearchKeywordUseCase: DeleteRecentSearchKeywordUseCase,
    private val getViewedHospitalsUseCase: GetViewedHospitalsUseCase,
    private val addViewedHospitalUseCase: AddViewedHospitalUseCase,
    private val deleteViewedHospitalUseCase: DeleteViewedHospitalUseCase,
    private val syncSearchHistoryUseCase: SyncSearchHistoryUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<HospitalSearchDataState>(HospitalSearchDataState.Init)
    val dataState: StateFlow<HospitalSearchDataState> = _dataState

    private val _eventFlow = MutableSharedFlow<SearchEvent>()
    val eventFlow: SharedFlow<SearchEvent> = _eventFlow

    private val _hospitalSearchQuery = MutableStateFlow(HospitalSearchQueryUiModel.empty)
    val hospitalSearchQuery: StateFlow<HospitalSearchQueryUiModel> = _hospitalSearchQuery

    private val _hospitalList = MutableStateFlow<List<Hospital>>(emptyList())
    val hospitalList: StateFlow<List<Hospital>> = _hospitalList

    private val _recentSearchKeywords = MutableStateFlow<List<RecentSearchKeyword>>(emptyList())
    val recentSearchKeywords: StateFlow<List<RecentSearchKeyword>> = _recentSearchKeywords

    private val _viewedHospitals =
        MutableStateFlow(ViewedHospitalList.stub().copy(items = emptyList(), totalCount = 0))
    val viewedHospitals: StateFlow<ViewedHospitalList> = _viewedHospitals

    // --- Cursor Pagination State ---
    private var currentCursorId: Long? = null
    private var currentCursorDistance: Double? = null
    private var currentCursorRating: Double? = null
    private var currentCursorReviewCount: Long? = null
    private var hasNextPage: Boolean = false
    private var isRequesting: Boolean = false

    // Keep track of last successful search params for "Load More"
    private var lastQueryModel: HospitalSearchQueryUiModel = HospitalSearchQueryUiModel.empty
    private var lastBounds: MapBounds? = null
    private var lastUserLocation: Location = Location("Default")
    private var lastSortType: HospitalSortType = HospitalSortType.DISTANCE

    fun onIntent(intent: HospitalSearchIntent) {
        when (intent) {
            is HospitalSearchIntent.UpdateSearchQuery -> {
                _hospitalSearchQuery.value = intent.query
            }

            is HospitalSearchIntent.SearchHospitalWithCurrentParams -> {
                _hospitalSearchQuery.value = intent.query
                launch {
                    val boundsToUse = if (intent.keepPreviousBounds) lastBounds else null

                    searchHospitals(
                        isNewSearch = true,
                        queryModel = intent.query,
                        currentUserLocation = intent.currentUserLocation,
                        sortType = intent.sortType,
                        bounds = boundsToUse
                    )
                }
            }

            is HospitalSearchIntent.SearchNearByHospitals -> {
                launch {
                    searchHospitals(
                        isNewSearch = true,
                        queryModel = intent.query,
                        currentUserLocation = intent.currentUserLocation,
                        sortType = intent.sortType,
                        bounds = intent.bounds
                    )
                }
            }

            is HospitalSearchIntent.LoadNextPage -> {
                if (hasNextPage && !isRequesting) {
                    launch {
                        searchHospitals(
                            isNewSearch = false,
                            queryModel = lastQueryModel,
                            currentUserLocation = lastUserLocation,
                            sortType = lastSortType,
                            bounds = lastBounds
                        )
                    }
                }
            }

            is HospitalSearchIntent.AddRecentKeyword -> {
                launch { addRecentSearchKeyword(intent.keyword) }
            }

            is HospitalSearchIntent.DeleteRecentKeyword -> {
                launch { deleteRecentSearchKeyword(intent.keyword) }
            }

            is HospitalSearchIntent.AddViewedHospital -> {
                launch { addViewedHospital(intent.hospitalId, intent.hospitalName) }
            }

            is HospitalSearchIntent.DeleteViewedHospital -> {
                launch { deleteViewedHospital(intent.hospitalId) }
            }
        }
    }

    init {
        observeErrorEvent(eventFlow)
        fetchRecentKeywords()
        fetchViewedHospitals()
        syncSearchHistoryFromServer()
    }

    private fun syncSearchHistoryFromServer() {
        launch {
            runCatching {
                syncSearchHistoryUseCase()
            }.onFailure {
                // 동기화 실패 시 로컬 데이터 사용
                it.printStackTrace()
            }
        }
    }


    private suspend fun searchHospitals(
        isNewSearch: Boolean,
        queryModel: HospitalSearchQueryUiModel,
        currentUserLocation: Location,
        sortType: HospitalSortType,
        bounds: MapBounds? = null
    ) {
        if (isRequesting) return
        isRequesting = true
        _dataState.value = HospitalSearchDataState.OnProgress

        if (isNewSearch) {
            resetCursors()
            _hospitalList.value = emptyList()
        }

        // Cache params
        lastQueryModel = queryModel
        lastUserLocation = currentUserLocation
        lastSortType = sortType
        lastBounds = bounds

        val regionParam = if (queryModel.region == null || queryModel.region == Region.ALL) {
            null
        } else {
            val district = queryModel.district
            if (district.isNullOrBlank() || district.contains("전체")) {
                queryModel.region.displayName.replace(" ", "")
            } else {
                "${queryModel.region.displayName}${district}".replace(" ", "")
            }
        }

        // 2. Animal 파라미터 처리
        val animalParam = if (queryModel.animalCategories.isEmpty()) {
            null
        } else {
            queryModel.animalCategories.joinToString(",") { it.name }
        }

        // 3. 검색어(q) 처리 (빈값 null)
        val qParam = queryModel.query?.trim()?.takeIf { it.isNotEmpty() }

        runCatching {
            searchHospitalsUseCase(
                q = qParam,
                region = regionParam,
                bounds = bounds,
                animal = animalParam,
                openNow = queryModel.openNowOnly,
                sortBy = sortType.name,
                size = 20,
                cursorId = currentCursorId,
                cursorDistance = currentCursorDistance,
                cursorRating = currentCursorRating,
                cursorReviewCount = currentCursorReviewCount
            )
        }.onSuccess { result ->
            val newItems = result.content.map { hospital ->
                val distance = LocationUtils.calculateDistance(
                    currentUserLocation.latitude,
                    currentUserLocation.longitude,
                    hospital.lat,
                    hospital.lng
                )
                hospital.copy(distanceMeters = distance)
            }

            if (isNewSearch) {
                _hospitalList.value = newItems
            } else {
                _hospitalList.value += newItems
            }

            hasNextPage = result.hasNext
            currentCursorId = result.cursorId
            currentCursorDistance = result.cursorDistance
            currentCursorRating = result.cursorRating
            currentCursorReviewCount = result.cursorReviewCount
        }.onFailure { ex ->
            _eventFlow.emit(
                SearchEvent.DataFetch.Error(
                    userMessage = "병원 목록을 불러오는데 실패했어요",
                    exceptionMessage = ex.message
                )
            )
        }

        _dataState.value = HospitalSearchDataState.Init
        isRequesting = false
    }

    private fun getBackendAnimalTypes(category: AnimalCategory): String {
        return AnimalSpecies.entries
            .filter { it.category == category }
            .joinToString(",") { it.name }
    }

    private fun resetCursors() {
        currentCursorId = null
        currentCursorDistance = null
        currentCursorRating = null
        currentCursorReviewCount = null
        hasNextPage = false
    }

    private fun fetchRecentKeywords() = launch {
        getRecentSearchKeywordUseCase()
            .onStart { _dataState.value = HospitalSearchDataState.OnProgress }
            .catch { ex ->
                _eventFlow.emit(SearchEvent.DataFetch.Error("최근 검색기록 조회 실패", ex.message))
                _dataState.value = HospitalSearchDataState.Init
            }
            .collect {
                _recentSearchKeywords.value = it
                _dataState.value = HospitalSearchDataState.Init
            }
    }

    private fun fetchViewedHospitals() = launch {
        getViewedHospitalsUseCase()
            .catch { ex ->
                _eventFlow.emit(SearchEvent.DataFetch.Error("최근 본 병원 조회 실패", ex.message))
            }
            .collect {
                _viewedHospitals.value =
                    ViewedHospitalList(items = it, totalCount = it.size.toLong())
            }
    }

    private suspend fun addRecentSearchKeyword(keyword: String) {
        runCatching { addSearchKeywordUseCase(keyword) }
            .onFailure { _eventFlow.emit(SearchEvent.DataFetch.Error("키워드 추가 실패", it.message)) }
    }

    private suspend fun deleteRecentSearchKeyword(keyword: String) {
        runCatching { deleteRecentSearchKeywordUseCase(keyword) }
            .onFailure { _eventFlow.emit(SearchEvent.DataFetch.Error("키워드 삭제 실패", it.message)) }
    }

    private suspend fun addViewedHospital(hospitalId: Long, hospitalName: String) {
        runCatching { addViewedHospitalUseCase(hospitalId, hospitalName) }
            .onFailure { _eventFlow.emit(SearchEvent.DataFetch.Error("본 병원 추가 실패", it.message)) }
    }

    private suspend fun deleteViewedHospital(hospitalId: Long) {
        runCatching { deleteViewedHospitalUseCase(hospitalId) }
            .onFailure { _eventFlow.emit(SearchEvent.DataFetch.Error("본 병원 삭제 실패", it.message)) }
    }
}