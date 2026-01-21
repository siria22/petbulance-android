package com.petbulance.presentation.screen.feature.search.main

import android.location.Location
import com.petbulance.domain.model.feature.hospital.hospital.Hospital
import com.petbulance.domain.model.feature.hospital.hospital.MapBounds
import com.petbulance.presentation.component.ui.molecule.FilterBottomSheetTab
import com.petbulance.domain.model.type.HospitalSortType
import com.petbulance.presentation.screen.feature.search.main.views.search.HospitalSearchQueryUiModel
import com.naver.maps.map.NaverMap

/**
 * 검색 화면 전체에서 공유하는 UI 상태
 */
data class SearchUiState(
    val hospitalList: List<Hospital>,
    val currentQuery: HospitalSearchQueryUiModel,
    val selectedSortType: HospitalSortType = HospitalSortType.DISTANCE,
    val isOpenNowOnly: Boolean = false,
    val isFilterBottomSheetVisible: Boolean = false,
    val currentSelectedFilterBottomSheet: FilterBottomSheetTab = FilterBottomSheetTab.REGION,
    val isSelectSortTypeDialogVisible: Boolean = false,
    val currentUserLocation: Location = Location("Default").apply { latitude = 37.57; longitude = 126.98 }
) {
    val filteredHospitalList: List<Hospital>
        get() = hospitalList
            .asSequence()
            .filter { if (isOpenNowOnly) it.isOpenNow else true }
            .sortedByDescending {
                when (selectedSortType) {
                    HospitalSortType.DISTANCE -> it.distanceMeters
                    HospitalSortType.REVIEW -> it.reviewCount?.toDouble()
                    HospitalSortType.RATING -> it.rating
                }
            }
            .toList()
}

/**
 * 검색 화면 전체에서 발생하는 UI 이벤트
 */
sealed interface SearchUiEvent {
    // --- Overlay & Filter Controls ---
    data class OnFilterButtonClicked(val tab: FilterBottomSheetTab) : SearchUiEvent
    data class OnSortTypeClicked(val isVisible: Boolean) : SearchUiEvent
    data class OnSortTypeSelected(val sortType: HospitalSortType) : SearchUiEvent
    data class OnDismissFilterBottomSheet(val isVisible: Boolean) : SearchUiEvent

    data object OnOpenNowOnlyClicked : SearchUiEvent
    data class OnQuerySet(val query: HospitalSearchQueryUiModel) : SearchUiEvent
    data object OnResetFilterClicked : SearchUiEvent

    // --- Search Actions ---
    data object OnSearchButtonClicked : SearchUiEvent
    data class OnSearchNearby(val bounds: MapBounds) : SearchUiEvent
    data class OnQueryChanged(val query: String) : SearchUiEvent

    // --- Navigation ---
    data object OnCurrentLocationClicked : SearchUiEvent
    data object OnListViewClicked : SearchUiEvent
    data object OnSearchViewClicked : SearchUiEvent
    data object OnNavigateToMapView : SearchUiEvent
    data object OnNavigateToResultView : SearchUiEvent

    // --- Map Specific ---
    data class OnMapReady(val naverMap: NaverMap) : SearchUiEvent

    // --- Recent Search / Viewed Hospital (SearchView Specific) ---
    data class OnRecentKeywordClicked(val keyword: String) : SearchUiEvent
    data class OnDeleteRecentKeyword(val keyword: String) : SearchUiEvent
    data class OnRecentHospitalClicked(val hospitalName: String) : SearchUiEvent
    data class OnDeleteRecentViewedHospital(val hospitalId: Long) : SearchUiEvent
}