package com.petbulance.presentation.screen.feature.mypage.profile

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.profileDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.MyPage.Profile.route,
//        arguments = listOf(
//            navArgument(name = "") {
//                type = NavType.LongType
//                defaultValue = 0L
//            }
//        ) -> if route contains arguments
    ) {
        val viewModel: ProfileViewModel = hiltViewModel()

        val argument: ProfileArgument = let {
            val dataState by viewModel.dataState.collectAsStateWithLifecycle()
            val screenState by viewModel.screenState.collectAsStateWithLifecycle()

            ProfileArgument(
                dataState = dataState,
                screenState = screenState,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data: ProfileData = let {
            val someData by viewModel.someData.collectAsStateWithLifecycle()

            ProfileData(
                data = someData
            )
        }

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            ProfileScreen(
                navController = navController,
                argument = argument,
                data = data
            )
        }
    }
}