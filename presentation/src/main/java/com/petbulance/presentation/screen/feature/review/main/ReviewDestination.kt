package com.petbulance.presentation.screen.feature.review.main

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.reviewDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Review.route
    ) {
        val viewModel: ReviewViewModel = hiltViewModel()

        val argument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()
            ReviewArgument(
                state = state,
                intent = viewModel::onIntent,
                event = viewModel.event
            )
        }

        val data by viewModel.reviewData.collectAsStateWithLifecycle()

        ReviewScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
