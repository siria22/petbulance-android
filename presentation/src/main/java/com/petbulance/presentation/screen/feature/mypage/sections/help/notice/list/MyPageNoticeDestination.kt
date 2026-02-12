package com.petbulance.presentation.screen.feature.mypage.sections.help.notice.list

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.myPageNoticeDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.MyPage.Help.Notice.route,
    ) {
        val viewModel: MyPageNoticeViewModel = hiltViewModel()

        val argument: MyPageNoticeArgument = let {
            val dataState by viewModel.dataState.collectAsStateWithLifecycle()
            val screenState by viewModel.screenState.collectAsStateWithLifecycle()

            MyPageNoticeArgument(
                dataState = dataState,
                screenState = screenState,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data: MyPageNoticeData = let {
            val notices by viewModel.notices.collectAsStateWithLifecycle()
            val isLoadingNextPage by viewModel.isLoadingNextPage.collectAsStateWithLifecycle()

            MyPageNoticeData(
                notices = notices,
                isLoadingNextPage = isLoadingNextPage
            )
        }

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            MyPageNoticeScreen(
                navController = navController,
                argument = argument,
                data = data
            )
        }
    }
}