package com.example.presentation.screen.feature.search.views.map

import android.location.Location
import com.example.domain.model.feature.hospital.hospital.Hospital
import com.example.presentation.component.ui.molecule.FilterBottomSheetTab
import com.example.presentation.screen.feature.search.views.common.HospitalSortType
import com.example.presentation.screen.feature.search.views.search.HospitalSearchQueryUiModel
import com.naver.maps.map.NaverMap

sealed interface MapViewArguments {
    data class OnFilterButtonClicked(val tab: FilterBottomSheetTab) : MapViewArguments
    data class OnSortTypeClicked(val isVisible: Boolean) : MapViewArguments
    data class OnSortTypeSelected(val sortType: HospitalSortType) : MapViewArguments
    data object OnOpenNowOnlyClicked : MapViewArguments
    data object OnCurrentLocationClicked : MapViewArguments
    data object OnListViewClicked : MapViewArguments
    data object OnRecenterSearchClicked : MapViewArguments
    data class OnMapReady(val naverMap: NaverMap) : MapViewArguments
    data class OnDismissFilterBottomSheet(val isVisible: Boolean) : MapViewArguments
    data class OnQuerySet(val query: HospitalSearchQueryUiModel) : MapViewArguments
    data object OnResetFilterClicked : MapViewArguments
    data object OnSearchButtonClicked : MapViewArguments
    data object OnSearchViewClicked : MapViewArguments
}

data class MapUiState(
    val hospitalList: List<Hospital>,
    val currentQuery: HospitalSearchQueryUiModel,
    val selectedSortType: HospitalSortType = HospitalSortType.DISTANCE,
    val isOpenNowOnly: Boolean = false,
    val isFilterBottomSheetVisible: Boolean = false,
    val currentSelectedFilterBottomSheet: FilterBottomSheetTab = FilterBottomSheetTab.REGION,
    val isSelectSortTypeDialogVisible: Boolean = false,
    val currentUserLocation: Location? = null
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