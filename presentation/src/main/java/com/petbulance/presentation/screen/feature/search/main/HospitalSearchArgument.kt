package com.petbulance.presentation.screen.feature.search.main

import android.location.Location
import com.petbulance.domain.model.feature.hospital.hospital.MapBounds
import com.petbulance.domain.model.type.HospitalSortType
import com.petbulance.presentation.screen.feature.search.main.views.search.HospitalSearchQueryUiModel

data class HospitalSearchArgument(
    val intent: (HospitalSearchIntent) -> Unit,
    val hospitalDataState: HospitalSearchDataState,
)

sealed class HospitalSearchDataState {
    data object Init : HospitalSearchDataState()
    data object OnProgress : HospitalSearchDataState()
}

sealed class HospitalSearchIntent {
    data class UpdateSearchQuery(val query: HospitalSearchQueryUiModel) : HospitalSearchIntent()

    data class SearchHospitalWithCurrentParams(
        val query: HospitalSearchQueryUiModel,
        val currentUserLocation: Location,
        val sortType: HospitalSortType
    ) : HospitalSearchIntent()

    data class SearchNearByHospitals(
        val bounds: MapBounds,
        val query: HospitalSearchQueryUiModel,
        val currentUserLocation: Location,
        val sortType: HospitalSortType
    ) : HospitalSearchIntent()

    data object LoadNextPage : HospitalSearchIntent()

    data class AddRecentKeyword(val keyword: String) : HospitalSearchIntent()
    data class DeleteRecentKeyword(val keyword: String) : HospitalSearchIntent()
    data class AddViewedHospital(val hospitalId: Long, val hospitalName: String) :
        HospitalSearchIntent()

    data class DeleteViewedHospital(val hospitalId: Long) : HospitalSearchIntent()
}