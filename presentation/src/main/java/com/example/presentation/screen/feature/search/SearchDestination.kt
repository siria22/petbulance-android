package com.example.presentation.screen.feature.search

import android.location.Location
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.presentation.screen.feature.search.views.search.HospitalSearchQueryUiModel
import com.example.presentation.utils.CommonScreenWrapper
import com.example.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.searchDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Search.route
    ) {
        val commonSearchViewModel: CommonSearchViewModel = hiltViewModel()
        val hospitalSearchViewModel: HospitalSearchViewModel = hiltViewModel()
        val userLocationViewModel: UserLocationViewModel = hiltViewModel()

        val commonSearchArgument: CommonSearchArgument = let {
            val screenState by commonSearchViewModel.screenState.collectAsStateWithLifecycle()

            CommonSearchArgument(
                screenState = screenState,
                event = commonSearchViewModel.eventFlow
            )
        }


        val locationState by userLocationViewModel.locationState.collectAsStateWithLifecycle()

        val userLocationArgument = UserLocationArgument(
            intent = userLocationViewModel::onIntent,
            locationState = locationState
        )

        val locationData: UserLocationData = let {
            val currentLoc: Location? = (locationState as? UserLocationState.Success)?.location

            if (currentLoc != null) UserLocationData(currentLoc)
            else UserLocationData.empty
        }

        val hospitalSearchArgument: HospitalSearchArgument = let {
            val dataState by hospitalSearchViewModel.dataState.collectAsStateWithLifecycle()

            HospitalSearchArgument(
                intent = hospitalSearchViewModel::onIntent,
                hospitalDataState = dataState
            )
        }

        val hospitalData: HospitalSearchData = let {
            val hospitalSearchQuery by hospitalSearchViewModel.hospitalSearchQuery.collectAsStateWithLifecycle()
            val hospitalList by hospitalSearchViewModel.hospitalList.collectAsStateWithLifecycle()
            val recentSearchKeywords by hospitalSearchViewModel.recentSearchKeywords.collectAsStateWithLifecycle()
            val viewedHospitals by hospitalSearchViewModel.viewedHospitals.collectAsStateWithLifecycle()
            HospitalSearchData(
                hospitalSearchQuery = hospitalSearchQuery ?: HospitalSearchQueryUiModel.empty,
                hospitalList = hospitalList,
                recentSearchKeywords = recentSearchKeywords,
                viewedHospitals = viewedHospitals
            )
        }

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