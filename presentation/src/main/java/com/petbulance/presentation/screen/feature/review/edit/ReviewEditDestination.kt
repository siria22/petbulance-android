package com.petbulance.presentation.screen.feature.review.edit

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.reviewEditDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Review.Edit.route,
        arguments = listOf(
            navArgument(name = ScreenDestinations.Review.Edit.ARG_ID) {
                type = NavType.LongType
            }
        )
    ) {
        val viewModel: ReviewEditViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        val argument = ReviewEditArgument(
            state = state,
            intent = viewModel::onIntent,
            event = viewModel.eventFlow
        )

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            ReviewEditScreen(
                navController = navController,
                argument = argument
            )
        }
    }
}