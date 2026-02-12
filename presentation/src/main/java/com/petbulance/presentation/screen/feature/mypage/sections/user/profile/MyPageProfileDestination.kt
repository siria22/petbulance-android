package com.petbulance.presentation.screen.feature.mypage.sections.user.profile

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.myPageProfileDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.MyPage.User.Profile.route,
//        arguments = listOf(
//            navArgument(name = "") {
//                type = NavType.LongType
//                defaultValue = 0L
//            }
//        ) -> if route contains arguments
    ) {
        val viewModel: MyPageProfileViewModel = hiltViewModel()

        val argument: MyPageProfileArgument = let {
            val dataState by viewModel.dataState.collectAsStateWithLifecycle()
            val screenState by viewModel.screenState.collectAsStateWithLifecycle()

            MyPageProfileArgument(
                dataState = dataState,
                screenState = screenState,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data: MyPageProfileData = let {
            val someData by viewModel.someData.collectAsStateWithLifecycle()

            MyPageProfileData(
                data = someData
            )
        }

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            MyPageProfileScreen(
                navController = navController,
                argument = argument,
                data = data
            )
        }
    }
}