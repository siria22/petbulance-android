package com.petbulance.presentation.screen.feature.review.search

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.reviewSearchDestination(navController: NavController) {
    composable(route = ScreenDestinations.Review.Search.route) {
        val viewModel: ReviewSearchViewModel = hiltViewModel()

        val state by viewModel.state.collectAsStateWithLifecycle()
        val searchDataBase by viewModel.searchData.collectAsStateWithLifecycle()
        val recentKeywords by viewModel.recentKeywords.collectAsStateWithLifecycle()

        val searchData = searchDataBase.copy(recentKeywords = recentKeywords)

        val argument = ReviewSearchArgument(
            state = state,
            intent = viewModel::onIntent,
            event = viewModel.eventFlow
        )

        ReviewSearchScreen(
            navController = navController,
            argument = argument,
            data = searchData
        )
    }
}
