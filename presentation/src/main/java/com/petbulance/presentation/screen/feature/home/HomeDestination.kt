package com.petbulance.presentation.screen.feature.home

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.homeDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Home.route
    ) {
        val viewModel: HomeViewModel = hiltViewModel()

        val argument: HomeArgument = let {
            val dataState by viewModel.dataState.collectAsStateWithLifecycle()
            val screenState by viewModel.screenState.collectAsStateWithLifecycle()

            HomeArgument(
                dataState = dataState,
                screenState = screenState,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data: HomeData = let {
            val recentReviews by viewModel.recentReviews.collectAsStateWithLifecycle()
            val hotArticle by viewModel.hotArticles.collectAsStateWithLifecycle()

            HomeData(
                recentReviews = recentReviews,
                hotArticles = hotArticle
            )
        }

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            HomeScreen(
                navController = navController,
                argument = argument,
                data = data
            )
        }
    }
}