package com.petbulance.presentation.screen.feature.mypage.sections.help.cs.coalition

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.coalitionDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.MyPage.Help.CS.Coalition.route,
    ) {
        val viewModel: CoalitionViewModel = hiltViewModel()

        val argument: CoalitionArgument = let {
            val dataState by viewModel.dataState.collectAsStateWithLifecycle()

            CoalitionArgument(
                dataState = dataState,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data by viewModel.formState.collectAsStateWithLifecycle()

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            CoalitionScreen(
                navController = navController,
                argument = argument,
                data = data
            )
        }
    }
}
