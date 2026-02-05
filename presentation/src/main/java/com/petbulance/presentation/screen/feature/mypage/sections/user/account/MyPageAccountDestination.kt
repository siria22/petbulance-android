package com.petbulance.presentation.screen.feature.mypage.sections.user.account

import MyPageAccountData
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.myPageAccountDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.MyPage.User.Account.route
    ) {
        val viewModel: MyPageAccountViewModel = hiltViewModel()

        val argument: MyPageAccountArgument = let {
            val dataState by viewModel.dataState.collectAsStateWithLifecycle()
            val screenState by viewModel.screenState.collectAsStateWithLifecycle()

            MyPageAccountArgument(
                dataState = dataState,
                screenState = screenState,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data by viewModel.uiState.collectAsStateWithLifecycle()
        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            MyPageAccountScreen(
                navController = navController,
                argument = argument,
                data = data
            )
        }
    }
}