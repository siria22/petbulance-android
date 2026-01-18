package com.example.presentation.screen.feature.review.main

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.presentation.utils.nav.ScreenDestinations

// ... imports

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

        val data = let {
            val reviews by viewModel.reviews.collectAsStateWithLifecycle()
            val region by viewModel.selectedRegion.collectAsStateWithLifecycle()
            val district by viewModel.selectedDistrict.collectAsStateWithLifecycle()
            val animal by viewModel.selectedAnimalType.collectAsStateWithLifecycle()
            val sort by viewModel.selectedSort.collectAsStateWithLifecycle()
            val isReceipt by viewModel.isReceiptVerified.collectAsStateWithLifecycle()
            val isPhoto by viewModel.isPhotoReview.collectAsStateWithLifecycle()
            val loadingNext by viewModel.isLoadingNextPage.collectAsStateWithLifecycle()

            ReviewData(
                reviews = reviews,
                selectedRegion = region,
                selectedDistrict = district,
                selectedAnimalType = animal,
                selectedSort = sort,
                isReceiptVerified = isReceipt,
                isPhotoReview = isPhoto,
                isLoadingNextPage = loadingNext
            )
        }

        ReviewScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}