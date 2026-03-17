package com.petbulance.presentation.screen.feature.community

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.petbulance.presentation.screen.feature.community.search.CommunitySearchArgument
import com.petbulance.presentation.screen.feature.community.search.CommunitySearchViewModel
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.communityDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Community.route,
    ) {
        val communityViewModel: CommunityViewModel = hiltViewModel()
        val searchViewModel: CommunitySearchViewModel = hiltViewModel()

        // CommunityArgument
        val dataState by communityViewModel.dataState.collectAsStateWithLifecycle()
        val screenState by communityViewModel.screenState.collectAsStateWithLifecycle()
        val argument = CommunityArgument(
            dataState = dataState,
            screenState = screenState,
            intent = communityViewModel::onIntent,
            event = communityViewModel.eventFlow
        )

        // CommunityData
        val noticeBanner by communityViewModel.noticeBanner.collectAsStateWithLifecycle()
        val posts by communityViewModel.posts.collectAsStateWithLifecycle()
        val hasNext by communityViewModel.hasNext.collectAsStateWithLifecycle()
        val currentType by communityViewModel.currentType.collectAsStateWithLifecycle()
        val currentTopic by communityViewModel.currentTopic.collectAsStateWithLifecycle()
        val currentSort by communityViewModel.currentSort.collectAsStateWithLifecycle()
        val data = CommunityData(
            noticeBanner = noticeBanner,
            posts = posts,
            hasNext = hasNext,
            currentType = currentType,
            currentTopic = currentTopic,
            currentSort = currentSort
        )

        // CommunitySearchArgument
        val searchDataState by searchViewModel.dataState.collectAsStateWithLifecycle()
        val searchArgument = CommunitySearchArgument(
            dataState = searchDataState,
            intent = searchViewModel::onIntent,
            event = searchViewModel.eventFlow
        )

        // CommunitySearchData
        val searchData by searchViewModel.data.collectAsStateWithLifecycle()
        val recentKeywords by searchViewModel.recentKeywords.collectAsStateWithLifecycle()

        val errorState by communityViewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = communityViewModel::dismissErrorDialog,
        ) {
            CommunityScreen(
                navController = navController,
                argument = argument,
                data = data,
                searchArgument = searchArgument,
                searchData = searchData,
                recentKeywords = recentKeywords
            )
        }
    }
}