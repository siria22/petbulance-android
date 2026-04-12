package com.petbulance.presentation.screen.feature.mypage.sections.activity.reviews

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.myPageReviewsDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.MyPage.Activity.Reviews.route
    ) {
        val viewModel: MyPageReviewsViewModel = hiltViewModel()

        val argument: MyPageReviewsArgument = let {
            val dataState by viewModel.dataState.collectAsStateWithLifecycle()
            val screenState by viewModel.screenState.collectAsStateWithLifecycle()

            MyPageReviewsArgument(
                dataState = dataState,
                screenState = screenState,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data: MyPageReviewsData = MyPageReviewsData.empty

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            MyPageReviewsScreen(
                navController = navController,
                argument = argument,
                data = data
            )
        }
    }
}