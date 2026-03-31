package com.petbulance.presentation.screen.feature.review.detail

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

fun NavGraphBuilder.reviewDetailDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Review.Detail.route,
        arguments = listOf(
            navArgument(name = ScreenDestinations.Review.Detail.ARG_ID) {
                type = NavType.LongType
                defaultValue = 0L
            }
        )
    ) {
        val viewModel: ReviewDetailViewModel = hiltViewModel()

        val argument: ReviewDetailArgument = let {
            val dataState by viewModel.dataState.collectAsStateWithLifecycle()

            ReviewDetailArgument(
                dataState = dataState,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data: ReviewDetailData = let {
            val reviewDetailData by viewModel.reviewDetailData.collectAsStateWithLifecycle()
            reviewDetailData
        }

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            ReviewDetailScreen(
                navController = navController,
                argument = argument,
                data = data
            )
        }
    }
}