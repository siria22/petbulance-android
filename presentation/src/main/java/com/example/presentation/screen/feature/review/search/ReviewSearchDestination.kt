package com.example.presentation.screen.feature.review.search

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.reviewSearchDestination(navController: NavController) {
    composable(route = ScreenDestinations.Review.Search.route) {
        val viewModel: ReviewSearchViewModel = hiltViewModel()

        val query by viewModel.query.collectAsStateWithLifecycle()
        val recentKeywords by viewModel.recentKeywords.collectAsStateWithLifecycle()
        val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
        val isSearchResultMode by viewModel.isSearchResultMode.collectAsStateWithLifecycle()

        val selectedRegion by viewModel.selectedRegion.collectAsStateWithLifecycle()
        val selectedDistrict by viewModel.selectedDistrict.collectAsStateWithLifecycle()
        val selectedAnimalType by viewModel.selectedAnimalType.collectAsStateWithLifecycle()
        val selectedSort by viewModel.selectedSort.collectAsStateWithLifecycle()
        val isReceiptVerified by viewModel.isReceiptVerified.collectAsStateWithLifecycle()
        val isPhotoReview by viewModel.isPhotoReview.collectAsStateWithLifecycle()

        val searchData = ReviewSearchData(
            query = query,
            recentKeywords = recentKeywords,
            searchResults = searchResults,
            isSearchResultMode = isSearchResultMode,
            isLoadingNextPage = false,
            selectedRegion = selectedRegion,
            selectedDistrict = selectedDistrict,
            selectedAnimalType = selectedAnimalType,
            selectedSort = selectedSort,
            isReceiptVerified = isReceiptVerified,
            isPhotoReview = isPhotoReview
        )

        val argument = ReviewSearchArgument(
            state = ReviewSearchState.Init,
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