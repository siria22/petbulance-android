package com.petbulance.presentation.screen.feature.review.camera

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.receiptCameraDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Review.ReceiptCamera.route
    ) {
        val viewModel: ReceiptCameraViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        val argument = ReceiptCameraArgument(
            state = state,
            intent = viewModel::onIntent,
            event = viewModel.event
        )

        val data = ReceiptCameraData(
            isAnalyzing = state.isAnalyzing
        )

        ReceiptCameraScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}