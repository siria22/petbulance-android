package com.example.presentation.screen.feature.review.create

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.reviewCreateDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Review.Create.route
    ) {
        val viewModel: ReviewCreateViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        val argument = ReviewCreateArgument(
            state = state,
            intent = viewModel::onIntent,
            event = viewModel.eventFlow
        )

        ReviewCreateScreen(
            navController = navController,
            argument = argument
        )
    }
}