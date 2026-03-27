package com.petbulance.presentation.screen.feature.mypage.main

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.myPageDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.MyPage.route
    ) {
        val viewModel: MyPageViewModel = hiltViewModel()

        val argument = MyPageArgument(
            intent = {},
            event = viewModel.eventFlow
        )

        val data: MyPageData = let {
            val userInfo by viewModel.userInfo.collectAsStateWithLifecycle()
            val currentVersion by viewModel.currentVersion.collectAsStateWithLifecycle()
            val latestVersion by viewModel.latestVersion.collectAsStateWithLifecycle()

            MyPageData(
                userInfo = userInfo,
                currentVersion = currentVersion,
                latestVersion = latestVersion
            )
        }

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            MyPageScreen(
                navController = navController,
                argument = argument,
                data = data
            )
        }
    }
}
