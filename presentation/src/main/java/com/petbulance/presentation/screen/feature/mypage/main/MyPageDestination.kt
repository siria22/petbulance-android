package com.petbulance.presentation.screen.feature.mypage.main

import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
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
        val context = LocalContext.current

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

        LaunchedEffect(Unit) {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is MyPageEvent.AccountSuspended -> {
                        Toast.makeText(
                            context,
                            "이용이 정지된 계정입니다. 고객센터에 문의해 주세요.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else -> {}
                }
            }
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
