package com.petbulance.presentation.screen.feature.mypage.sections.help.cs.coalition

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.coalitionDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.MyPage.Help.CS.Coalition.route,
    ) {
        val viewModel: CoalitionViewModel = hiltViewModel()

        val argument: CoalitionArgument = let {
            val dataState by viewModel.dataState.collectAsStateWithLifecycle()
            val screenState by viewModel.screenState.collectAsStateWithLifecycle()

            CoalitionArgument(
                dataState = dataState,
                screenState = screenState,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data: CoalitionData = let {
            val inquiryType by viewModel.inquiryType.collectAsStateWithLifecycle()
            val companyName by viewModel.companyName.collectAsStateWithLifecycle()
            val managerName by viewModel.managerName.collectAsStateWithLifecycle()
            val managerPosition by viewModel.managerPosition.collectAsStateWithLifecycle()
            val phone by viewModel.phone.collectAsStateWithLifecycle()
            val email by viewModel.email.collectAsStateWithLifecycle()
            val interestTypes by viewModel.interestTypes.collectAsStateWithLifecycle()
            val content by viewModel.content.collectAsStateWithLifecycle()
            val privacyConsent by viewModel.privacyConsent.collectAsStateWithLifecycle()
            val isSubmitEnabled by viewModel.isSubmitEnabled.collectAsStateWithLifecycle()

            CoalitionData(
                inquiryType = inquiryType,
                companyName = companyName,
                managerName = managerName,
                managerPosition = managerPosition,
                phone = phone,
                email = email,
                interestTypes = interestTypes,
                content = content,
                privacyConsent = privacyConsent,
                isSubmitEnabled = isSubmitEnabled
            )
        }

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            CoalitionScreen(
                navController = navController,
                argument = argument,
                data = data
            )
        }
    }
}