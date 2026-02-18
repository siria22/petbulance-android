package com.petbulance.presentation.screen.feature.mypage.sections.help.notice.detail

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.myPageNoticeDetailDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.MyPage.Help.Notice.Detail.route,
        arguments = listOf(
            navArgument(ScreenDestinations.MyPage.Help.Notice.Detail.ARG_ID) {
                type = NavType.LongType
            }
        )
    ) {
        val viewModel: MyPageNoticeDetailViewModel = hiltViewModel()

        val argument: MyPageNoticeDetailArgument = let {
            val dataState by viewModel.dataState.collectAsStateWithLifecycle()
            val screenState by viewModel.screenState.collectAsStateWithLifecycle()

            MyPageNoticeDetailArgument(
                dataState = dataState,
                screenState = screenState,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data: MyPageNoticeDetailData = let {
            val noticeDetail by viewModel.noticeDetail.collectAsStateWithLifecycle()

            MyPageNoticeDetailData(noticeDetail = noticeDetail)
        }

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            MyPageNoticeDetailScreen(
                navController = navController,
                argument = argument,
                data = data
            )
        }
    }
}
