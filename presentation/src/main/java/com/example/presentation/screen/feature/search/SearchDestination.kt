package com.example.presentation.screen.feature.search

import android.location.Location
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.presentation.utils.CommonScreenWrapper
import com.example.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.searchDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Search.route
    ) {
        val commonSearchViewModel: CommonSearchViewModel = hiltViewModel()
        val hospitalSearchViewModel: HospitalSearchViewModel = hiltViewModel()
        val userLocationViewModel: UserLocationViewModel = hiltViewModel()

        // CommonSearchArgument
        val screenState by commonSearchViewModel.screenState.collectAsStateWithLifecycle()
        val commonSearchArgument = CommonSearchArgument(
            screenState = screenState,
            event = commonSearchViewModel.eventFlow,
            intent = commonSearchViewModel::onIntent
        )

        // UserLocationArgument
        val locationState by userLocationViewModel.locationState.collectAsStateWithLifecycle()
        val userLocationArgument = UserLocationArgument(
            intent = userLocationViewModel::onIntent,
            locationState = locationState
        )

        // LocationData
        val currentLoc: Location? = (locationState as? UserLocationState.Success)?.location
        val locationData =
            if (currentLoc != null) UserLocationData(currentLoc) else UserLocationData.empty

        // HospitalSearchArgument
        val dataState by hospitalSearchViewModel.dataState.collectAsStateWithLifecycle()
        val hospitalSearchArgument = HospitalSearchArgument(
            intent = hospitalSearchViewModel::onIntent,
            hospitalDataState = dataState
        )

        // HospitalSearchData
        val hospitalSearchQuery by hospitalSearchViewModel.hospitalSearchQuery.collectAsStateWithLifecycle()
        val hospitalList by hospitalSearchViewModel.hospitalList.collectAsStateWithLifecycle()
        val recentSearchKeywords by hospitalSearchViewModel.recentSearchKeywords.collectAsStateWithLifecycle()
        val viewedHospitals by hospitalSearchViewModel.viewedHospitals.collectAsStateWithLifecycle()

        val hospitalData = HospitalSearchData(
            hospitalSearchQuery = hospitalSearchQuery,
            hospitalList = hospitalList,
            recentSearchKeywords = recentSearchKeywords,
            viewedHospitals = viewedHospitals
        )

        // Error State
        val errorState by hospitalSearchViewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = hospitalSearchViewModel::dismissErrorDialog,
        ) {
            SearchScreen(
                navController = navController,
                userLocationArgument = userLocationArgument,
                hospitalSearchArgument = hospitalSearchArgument,
                commonSearchArgument = commonSearchArgument,
                locationData = locationData,
                hospitalSearchData = hospitalData
            )
        }
    }
}