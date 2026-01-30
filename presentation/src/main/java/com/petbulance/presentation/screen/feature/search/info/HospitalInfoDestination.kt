package com.petbulance.presentation.screen.feature.search.info

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.petbulance.presentation.screen.feature.search.main.UserLocationIntent
import com.petbulance.presentation.screen.feature.search.main.UserLocationState
import com.petbulance.presentation.screen.feature.search.main.UserLocationViewModel
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.ScreenDestinations.Search.HospitalInfo.ARG_ID

fun NavGraphBuilder.hospitalInfoDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Search.HospitalInfo.route,
        arguments = listOf(
            navArgument(name = ARG_ID) {
                type = NavType.LongType
                defaultValue = -1L
            }
        )
    ) {
        val viewModel: HospitalInfoViewModel = hiltViewModel()
        val userLocationViewModel: UserLocationViewModel = hiltViewModel()

        val dataState by viewModel.dataState.collectAsStateWithLifecycle()
        val argument = HospitalInfoArgument(
            state = dataState,
            intent = viewModel::onIntent,
            event = viewModel.eventFlow
        )

        val userLocationState by userLocationViewModel.locationState.collectAsStateWithLifecycle()
        val currentLocation = (userLocationState as? UserLocationState.Success)?.location
        var isDataLoaded by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            userLocationViewModel.onIntent(UserLocationIntent.RequestLocation)
        }

        LaunchedEffect(userLocationState) {
            if (isDataLoaded) return@LaunchedEffect

            when (val state = userLocationState) {
                is UserLocationState.Success -> {
                    viewModel.onIntent(HospitalInfoIntent.LoadData(state.location.latitude, state.location.longitude))
                    isDataLoaded = true
                }
                is UserLocationState.PermissionRequired, is UserLocationState.Failed -> {
                    viewModel.onIntent(HospitalInfoIntent.LoadData(null, null))
                    isDataLoaded = true
                }
                else -> {}
            }
        }

        val reviewUiData by viewModel.reviewUiData.collectAsStateWithLifecycle()
        val hospitalUiData by viewModel.hospitalUiData.collectAsStateWithLifecycle()
        val data = HospitalInfoData(
            reviewUiData = reviewUiData,
            hospitalUiData = hospitalUiData
        )

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            HospitalInfoScreen(
                navController = navController,
                argument = argument,
                data = data,
                currentLocation = currentLocation
            )
        }
    }
}