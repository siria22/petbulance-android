package com.petbulance.presentation.screen.feature.search.main

import android.location.Location
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.model.type.TermsType
import com.petbulance.presentation.screen.nonfeature.auth.AuthViewModel
import com.petbulance.presentation.screen.nonfeature.login.terms.TermsViewModel
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.searchDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Search.route,
        arguments = listOf(
            navArgument(ScreenDestinations.Search.ARG_ANIMAL) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
            navArgument(ScreenDestinations.Search.ARG_INITIAL_HOSPITAL_ID) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) { entry ->
        val commonSearchViewModel: CommonSearchViewModel = hiltViewModel()
        val hospitalSearchViewModel: HospitalSearchViewModel = hiltViewModel()
        val userLocationViewModel: UserLocationViewModel = hiltViewModel()
        val authViewModel : AuthViewModel = hiltViewModel()
        val termsViewModel: TermsViewModel = hiltViewModel()

        val initialHospitalIdString = entry.arguments?.getString(ScreenDestinations.Search.ARG_INITIAL_HOSPITAL_ID)
        val initialHospitalId = initialHospitalIdString?.toLongOrNull()

        LaunchedEffect(Unit) {
            val animalName = entry.arguments?.getString(ScreenDestinations.Search.ARG_ANIMAL)
            val category = AnimalCategory.entries.find { it.name == animalName }

            if (category != null) {
                val currentQuery = hospitalSearchViewModel.hospitalSearchQuery.value
                hospitalSearchViewModel.onIntent(
                    HospitalSearchIntent.UpdateSearchQuery(
                        currentQuery.copy(animalCategories = listOf(category))
                    )
                )
            }

            if (initialHospitalId != null) {
                commonSearchViewModel.onIntent(SearchIntent.ChangeScreenState(SearchScreenState.Hospitals.MapView))
            }
        }

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
            locationState = locationState,
            event = userLocationViewModel.eventFlow
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

        val isLoggedIn by authViewModel.isLoggedIn.collectAsStateWithLifecycle()
        // Error State
        val errorState by hospitalSearchViewModel.errorDialogState.collectAsStateWithLifecycle()

        // Terms Data
        val locationTerm by termsViewModel.currentTerm.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = hospitalSearchViewModel::dismissErrorDialog,
        ) {
            SearchScreen(
                navController = navController,
                isGuest = (isLoggedIn == false),
                userLocationArgument = userLocationArgument,
                hospitalSearchArgument = hospitalSearchArgument,
                commonSearchArgument = commonSearchArgument,
                locationData = locationData,
                hospitalSearchData = hospitalData,
                locationTerm = locationTerm,
                onTermsClick = { termsViewModel.loadTermDetail(TermsType.LOCATION.name) },
                initialHospitalId = initialHospitalId
            )
        }
    }
}