package com.petbulance.presentation.screen.feature.mypage.sections.user.permissionsettings

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.permissionSettingsDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.MyPage.User.PermissionSettings.route
    ) {
        val viewModel: PermissionSettingsViewModel = hiltViewModel()

        val argument: PermissionSettingsArgument = let {
            val dataState by viewModel.dataState.collectAsStateWithLifecycle()
            PermissionSettingsArgument(
                dataState = dataState,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data by viewModel.settingsData.collectAsStateWithLifecycle()

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            PermissionSettingsScreen(
                navController = navController,
                argument = argument,
                data = data
            )
        }
    }
}
