package com.petbulance.presentation.screen.feature.search.main

import android.location.Location
import com.petbulance.domain.model.feature.hospital.hospital.Hospital
import com.petbulance.domain.model.feature.hospital.hospital.MapBounds
import com.petbulance.domain.model.feature.hospital.recent.RecentSearchKeyword
import com.petbulance.domain.model.feature.hospital.recent.ViewedHospitalList
import com.petbulance.domain.usecase.feature.hospital.hospital.SearchHospitalsUseCase
import com.petbulance.domain.usecase.feature.hospital.recent.AddSearchKeywordUseCase
import com.petbulance.domain.usecase.feature.hospital.recent.AddViewedHospitalUseCase
import com.petbulance.domain.usecase.feature.hospital.recent.DeleteRecentSearchKeywordUseCase
import com.petbulance.domain.usecase.feature.hospital.recent.DeleteViewedHospitalUseCase
import com.petbulance.domain.usecase.feature.hospital.recent.GetRecentSearchKeywordUseCase
import com.petbulance.domain.usecase.feature.hospital.recent.GetViewedHospitalsUseCase
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
    private val deleteViewedHospitalUseCase: DeleteViewedHospitalUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<HospitalSearchDataState>(HospitalSearchDataState.Init)
    val dataState: StateFlow<HospitalSearchDataState> = _dataState

    private val _eventFlow = MutableSharedFlow<SearchEvent>()
    val eventFlow: SharedFlow<SearchEvent> = _eventFlow

    private val _hospitalSearchQuery = MutableStateFlow(HospitalSearchQueryUiModel.Companion.empty)
    val hospitalSearchQuery: StateFlow<HospitalSearchQueryUiModel> = _hospitalSearchQuery

    private val _hospitalList = MutableStateFlow<List<Hospital>>(emptyList())
    val hospitalList: StateFlow<List<Hospital>> = _hospitalList

    private val _recentSearchKeywords = MutableStateFlow<List<RecentSearchKeyword>>(emptyList())
    val recentSearchKeywords: StateFlow<List<RecentSearchKeyword>> = _recentSearchKeywords

    private val _viewedHospitals =
        MutableStateFlow(ViewedHospitalList.stub().copy(items = emptyList(), totalCount = 0))
    val viewedHospitals: StateFlow<ViewedHospitalList> = _viewedHospitals

    fun onIntent(intent: HospitalSearchIntent) {
        when (intent) {
            is HospitalSearchIntent.UpdateSearchQuery -> {
                _hospitalSearchQuery.value = intent.query
            }

            is HospitalSearchIntent.SearchHospitalWithCurrentParams -> {
                _hospitalSearchQuery.value = intent.query
                launch { searchHospitals(null, intent.query, intent.currentUserLocation) }
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

            is HospitalSearchIntent.SearchNearByHospitals -> {
                launch { searchHospitals(intent.bounds, intent.query, intent.currentUserLocation) }
            }
        }
    }

    init {
        observeErrorEvent(eventFlow)

        launch {
            getRecentSearchKeywordUseCase()
                .onStart { _dataState.value = HospitalSearchDataState.OnProgress }
                .catch { ex ->
                    _eventFlow.emit(
                        SearchEvent.DataFetch.Error(
                            userMessage = "최근 검색기록을 가져오는데 실패했어요",
                            exceptionMessage = ex.message
                        )
                    )
                    _dataState.value = HospitalSearchDataState.Init
                }
                .collect { keywords ->
                    _recentSearchKeywords.value = keywords
                    _dataState.value = HospitalSearchDataState.Init
                }
        }

        launch {
            getViewedHospitalsUseCase()
                .catch { ex ->
                    _eventFlow.emit(
                        SearchEvent.DataFetch.Error(
                            userMessage = "최근 본 병원 목록을 가져오는데 실패했어요",
                            exceptionMessage = ex.message
                        )
                    )
                }
                .collect { list ->
                    _viewedHospitals.value =
                        ViewedHospitalList(items = list, totalCount = list.size.toLong())
                }
        }
    }

    private suspend fun searchHospitals(
        bounds: MapBounds? = null,
        queryModel: HospitalSearchQueryUiModel,
        currentUserLocation: Location
    ) {
        _dataState.value = HospitalSearchDataState.OnProgress
        runCatching {
            searchHospitalsUseCase(
                q = queryModel.query,
                region = queryModel.getRegionFilter(),
                lat = currentUserLocation.latitude,
                lng = currentUserLocation.longitude,
                bounds = bounds,
                animal = queryModel.species?.name,
                openNow = queryModel.openNowOnly,
                page = 1,       // TODO()
                size = 10       // TODO : 페이징 어케 관리하지
            )
        }.onSuccess { result ->
            _hospitalList.value = result.content
        }.onFailure { ex ->
            _eventFlow.emit(
                SearchEvent.DataFetch.Error(
                    userMessage = "병원 목록을 불러오는데 실패했어요",
                    exceptionMessage = ex.message
                )
            )
        }
        _dataState.value = HospitalSearchDataState.Init
    }

    private suspend fun addRecentSearchKeyword(keyword: String) {
        runCatching {
            addSearchKeywordUseCase(keyword)
        }.onFailure { ex ->
            _eventFlow.emit(
                SearchEvent.DataFetch.Error(
                    userMessage = "최근 검색어 추가에 실패했어요",
                    exceptionMessage = ex.message
                )
            )
        }
    }

    private suspend fun deleteRecentSearchKeyword(keyword: String) {
        runCatching {
            deleteRecentSearchKeywordUseCase(keyword)
        }.onFailure { ex ->
            _eventFlow.emit(
                SearchEvent.DataFetch.Error(
                    userMessage = "최근 검색어 삭제에 실패했어요",
                    exceptionMessage = ex.message
                )
            )
        }
    }

    private suspend fun addViewedHospital(hospitalId: Long, hospitalName: String) {
        runCatching {
            addViewedHospitalUseCase(hospitalId, hospitalName)
        }.onFailure { ex ->
            _eventFlow.emit(
                SearchEvent.DataFetch.Error(
                    userMessage = "최근 본 병원 추가에 실패했어요",
                    exceptionMessage = ex.message
                )
            )
        }
    }

    private suspend fun deleteViewedHospital(hospitalId: Long) {
        runCatching {
            deleteViewedHospitalUseCase(hospitalId)
        }.onFailure { ex ->
            _eventFlow.emit(
                SearchEvent.DataFetch.Error(
                    userMessage = "최근 본 병원 삭제에 실패했어요",
                    exceptionMessage = ex.message
                )
            )
        }
    }
}
