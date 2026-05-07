package com.petbulance.presentation.screen.feature.mypage.sections.user.withdrawal

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.withdrawalDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.MyPage.User.Withdrawal.route
    ) {
        val viewModel: WithdrawalViewModel = hiltViewModel()

        val argument: WithdrawalArgument = let {
            val dataState by viewModel.dataState.collectAsStateWithLifecycle()
            WithdrawalArgument(
                dataState = dataState,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            WithdrawalScreen(
                navController = navController,
                argument = argument
            )
        }
    }
}
