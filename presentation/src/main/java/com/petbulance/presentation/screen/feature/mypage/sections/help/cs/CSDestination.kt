package com.petbulance.presentation.screen.feature.mypage.sections.help.cs

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.cSDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.MyPage.Help.CS.route,
    ) {
        val viewModel: CSViewModel = hiltViewModel()

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            CSScreen(navController = navController)
        }
    }
}