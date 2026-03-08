package com.petbulance.presentation.screen.feature.mypage.sections.help.cs.qna.list

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.qnaListDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.MyPage.Help.CS.Qna.List.route
    ) {
        val viewModel: QnaListViewModel = hiltViewModel()

        val argument: QnaListArgument = let {
            val dataState by viewModel.dataState.collectAsStateWithLifecycle()
            val screenState by viewModel.screenState.collectAsStateWithLifecycle()

            QnaListArgument(
                dataState = dataState,
                screenState = screenState,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data: QnaListData = let {
            val qnaList by viewModel.qnaList.collectAsStateWithLifecycle()
            val successMessage by viewModel.successMessage.collectAsStateWithLifecycle()

            QnaListData(
                qnaList = qnaList,
                successMessage = successMessage
            )
        }

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            QnaListScreen(
                navController = navController,
                argument = argument,
                data = data
            )
        }
    }
}
