package com.petbulance.presentation.screen.feature.search.main

import android.location.Location
import com.petbulance.domain.model.feature.hospital.hospital.Hospital
import com.petbulance.domain.model.feature.hospital.recent.RecentSearchKeyword
import com.petbulance.domain.model.feature.hospital.recent.ViewedHospitalList
import com.petbulance.presentation.screen.feature.search.main.views.search.HospitalSearchQueryUiModel

data class UserLocationData(
    val currentUserLocation: Location
) {
    companion object {
        val empty = UserLocationData(
            currentUserLocation = Location("empty").apply {
                latitude = 37.579690
                longitude = 126.977243
            }
        )
    }
}

data class HospitalSearchData(
    val hospitalSearchQuery: HospitalSearchQueryUiModel,
    val hospitalList: List<Hospital>,
    val recentSearchKeywords: List<RecentSearchKeyword>,
    val viewedHospitals: ViewedHospitalList
) {
    companion object {
        val empty = HospitalSearchData(
            HospitalSearchQueryUiModel.Companion.empty,
            emptyList(),
            emptyList(),
            ViewedHospitalList.empty
        )
    }
}