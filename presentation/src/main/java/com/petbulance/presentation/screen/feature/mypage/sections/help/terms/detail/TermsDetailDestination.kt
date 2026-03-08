package com.petbulance.presentation.screen.feature.mypage.sections.help.terms.detail

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

fun NavGraphBuilder.termsDetailDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.MyPage.Help.Terms.Detail.route,
        arguments = listOf(
            navArgument(ScreenDestinations.MyPage.Help.Terms.Detail.ARG_TERMS_TYPE) {
                type = NavType.StringType
            }
        )
    ) {
        val viewModel: TermsDetailViewModel = hiltViewModel()

        val argument: TermsDetailArgument = let {
            val dataState by viewModel.dataState.collectAsStateWithLifecycle()
            val screenState by viewModel.screenState.collectAsStateWithLifecycle()

            TermsDetailArgument(
                dataState = dataState,
                screenState = screenState,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data: TermsDetailData = let {
            val term by viewModel.term.collectAsStateWithLifecycle()
            val isAgreed by viewModel.isAgreed.collectAsStateWithLifecycle()

            TermsDetailData(
                term = term,
                isAgreed = isAgreed
            )
        }

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            TermsDetailScreen(
                navController = navController,
                argument = argument,
                data = data
            )
        }
    }
}
