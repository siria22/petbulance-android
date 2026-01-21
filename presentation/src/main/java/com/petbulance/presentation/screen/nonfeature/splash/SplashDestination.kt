package com.petbulance.presentation.screen.nonfeature.splash

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.splashDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Splash.route
    ) {
        val viewModel: SplashViewModel = hiltViewModel()

        val argument: SplashArgument = SplashArgument(
            intent = viewModel::onIntent,
            event = viewModel.event
        )

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            SplashScreen(
                navController = navController,
                argument = argument,
            )
        }
    }
}