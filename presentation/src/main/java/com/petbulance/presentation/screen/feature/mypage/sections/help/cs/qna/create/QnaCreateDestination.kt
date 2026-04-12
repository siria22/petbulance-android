package com.petbulance.presentation.screen.feature.mypage.sections.help.cs.qna.create

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

fun NavGraphBuilder.qnaCreateDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.MyPage.Help.CS.Qna.Create.route,
        arguments = listOf(
            navArgument(name = ScreenDestinations.MyPage.Help.CS.Qna.Create.ARG_QNA_ID) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) {
        val viewModel: QnaCreateViewModel = hiltViewModel()

        val argument: QnaCreateArgument = let {
            val dataState by viewModel.dataState.collectAsStateWithLifecycle()
            val screenState by viewModel.screenState.collectAsStateWithLifecycle()

            QnaCreateArgument(
                dataState = dataState,
                screenState = screenState,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data: QnaCreateData = let {
            val title by viewModel.title.collectAsStateWithLifecycle()
            val content by viewModel.content.collectAsStateWithLifecycle()
            val isSubmitEnabled by viewModel.isSubmitEnabled.collectAsStateWithLifecycle()
            val mode by viewModel.mode.collectAsStateWithLifecycle()

            QnaCreateData(
                title = title,
                content = content,
                isSubmitEnabled = isSubmitEnabled,
                mode = mode
            )
        }

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            QnaCreateScreen(
                navController = navController,
                argument = argument,
                data = data
            )
        }
    }
}