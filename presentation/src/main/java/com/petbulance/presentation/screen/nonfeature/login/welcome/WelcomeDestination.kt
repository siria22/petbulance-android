package com.petbulance.presentation.screen.nonfeature.login.welcome

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.petbulance.presentation.screen.nonfeature.login.terms.TermsData
import com.petbulance.presentation.screen.nonfeature.login.terms.TermsViewModel
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.welcomeDestination(navController: NavController) {
    composable(route = ScreenDestinations.Welcome.route) {
        val viewModel: TermsViewModel = hiltViewModel()

        val termsList by viewModel.termsList.collectAsStateWithLifecycle()
        val agreedTermIds by viewModel.agreedTermIds.collectAsStateWithLifecycle()
        val isAllRequiredAgreed by viewModel.isAllRequiredAgreed.collectAsStateWithLifecycle()
        val currentTerm by viewModel.currentTerm.collectAsStateWithLifecycle()
        val userTempName by viewModel.userTempName.collectAsStateWithLifecycle()

        val data = TermsData(
            termsList = termsList,
            agreedTermIds = agreedTermIds,
            isAllRequiredAgreed = isAllRequiredAgreed,
            currentTerm = currentTerm,
            userTempName = userTempName
        )

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog
        ) {
            WelcomeScreen(
                navController = navController,
                data = data,
                intent = viewModel::onIntent,
                event = viewModel.event
            )
        }
    }
}