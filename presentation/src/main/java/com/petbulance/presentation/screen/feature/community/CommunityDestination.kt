package com.petbulance.presentation.screen.feature.community

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.communityDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Community.route,
    ) {
        val viewModel: CommunityViewModel = hiltViewModel()

        val argument: CommunityArgument = let {
            val dataState by viewModel.dataState.collectAsStateWithLifecycle()
            val screenState by viewModel.screenState.collectAsStateWithLifecycle()

            CommunityArgument(
                dataState = dataState,
                screenState = screenState,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data: CommunityData = let {
            val noticeBanner by viewModel.noticeBanner.collectAsStateWithLifecycle()
            val posts by viewModel.posts.collectAsStateWithLifecycle()
            val hasNext by viewModel.hasNext.collectAsStateWithLifecycle()
            val currentType by viewModel.currentType.collectAsStateWithLifecycle()
            val currentTopic by viewModel.currentTopic.collectAsStateWithLifecycle()
            val currentSort by viewModel.currentSort.collectAsStateWithLifecycle()

            CommunityData(
                noticeBanner = noticeBanner,
                posts = posts,
                hasNext = hasNext,
                currentType = currentType,
                currentTopic = currentTopic,
                currentSort = currentSort
            )
        }

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            CommunityScreen(
                navController = navController,
                argument = argument,
                data = data
            )
        }
    }
}