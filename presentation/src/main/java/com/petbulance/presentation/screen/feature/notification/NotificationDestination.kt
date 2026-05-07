package com.petbulance.presentation.screen.feature.notification

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.notificationDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Notification.route
    ) {
        val viewModel: NotificationViewModel = hiltViewModel()

        val argument: NotificationArgument = let {
            val dataState by viewModel.dataState.collectAsStateWithLifecycle()
            NotificationArgument(
                dataState = dataState,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data by viewModel.notificationData.collectAsStateWithLifecycle()

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            NotificationScreen(
                navController = navController,
                argument = argument,
                data = data
            )
        }
    }
}
