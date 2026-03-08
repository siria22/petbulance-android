package com.petbulance.presentation.screen.feature.mypage.sections.help.terms

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.termsListDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.MyPage.Help.Terms.route
    ) {
        val viewModel: TermsListViewModel = hiltViewModel()

        val argument: TermsListArgument = let {
            val dataState by viewModel.dataState.collectAsStateWithLifecycle()
            val screenState by viewModel.screenState.collectAsStateWithLifecycle()

            TermsListArgument(
                dataState = dataState,
                screenState = screenState,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data: TermsListData = let {
            val termsList by viewModel.termsList.collectAsStateWithLifecycle()

            TermsListData(
                termsList = termsList
            )
        }

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            TermsListScreen(
                navController = navController,
                argument = argument,
                data = data
            )
        }
    }
}
