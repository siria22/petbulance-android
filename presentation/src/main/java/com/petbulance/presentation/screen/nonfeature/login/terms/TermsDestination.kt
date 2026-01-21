package com.petbulance.presentation.screen.nonfeature.login.terms

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.termsDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Terms.route
    ) {
        val viewModel: TermsViewModel = hiltViewModel()

        val argument: TermsArgument = let {
            val dataState by viewModel.dataState.collectAsStateWithLifecycle()
            val screenState by viewModel.screenState.collectAsStateWithLifecycle()

            TermsArgument(
                dataState = dataState,
                screenState = screenState,
                intent = viewModel::onIntent,
                event = viewModel.event
            )
        }

        val data: TermsData = let {
            val termsList by viewModel.termsList.collectAsStateWithLifecycle()
            val agreedTermIds by viewModel.agreedTermIds.collectAsStateWithLifecycle()
            val isAllRequiredAgreed by viewModel.isAllRequiredAgreed.collectAsStateWithLifecycle()
            val currentTerm by viewModel.currentTerm.collectAsStateWithLifecycle()

            TermsData(
                termsList = termsList,
                agreedTermIds = agreedTermIds,
                isAllRequiredAgreed = isAllRequiredAgreed,
                currentTerm = currentTerm // 추가
            )
        }

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog
        ) {
            TermsScreen(
                navController = navController,
                argument = argument,
                data = data
            )
        }
    }
}