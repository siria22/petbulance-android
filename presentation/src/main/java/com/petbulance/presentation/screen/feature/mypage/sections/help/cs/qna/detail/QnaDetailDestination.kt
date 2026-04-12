package com.petbulance.presentation.screen.feature.mypage.sections.help.cs.qna.detail

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.petbulance.domain.model.feature.support.qna.QnaStatus
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.qnaDetailDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.MyPage.Help.CS.Qna.Detail.route,
        arguments = listOf(
            navArgument(name = ScreenDestinations.MyPage.Help.CS.Qna.Detail.ARG_ID) {
                type = NavType.LongType
                defaultValue = 0L
            }
        )
    ) {
        val viewModel: QnaDetailViewModel = hiltViewModel()

        val argument: QnaDetailArgument = let {
            val dataState by viewModel.dataState.collectAsStateWithLifecycle()

            QnaDetailArgument(
                dataState = dataState,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data: QnaDetailData = let {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val qna = uiState.qna

            QnaDetailData(
                qnaId = qna?.id ?: 0L,
                title = qna?.title ?: "",
                content = qna?.content ?: "",
                date = qna?.date ?: "",
                status = qna?.status ?: QnaStatus.ANSWER_WAITING,
                answer = qna?.answer,
                isLoading = uiState.isLoading
            )
        }

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            QnaDetailScreen(
                navController = navController,
                argument = argument,
                data = data
            )
        }
    }
}