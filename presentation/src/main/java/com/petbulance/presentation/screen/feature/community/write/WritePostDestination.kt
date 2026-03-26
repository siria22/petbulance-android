package com.petbulance.presentation.screen.feature.community.write

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

fun NavGraphBuilder.writePostDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Community.WritePost.route,
        arguments = listOf(
            navArgument(name = ScreenDestinations.Community.WritePost.ARG_POST_ID) {
                type = NavType.LongType
                defaultValue = ScreenDestinations.Community.WritePost.NO_POST_ID
            }
        )
    ) {
        val viewModel: WritePostViewModel = hiltViewModel()

        val argument: WritePostArgument = let {
            val dataState by viewModel.dataState.collectAsStateWithLifecycle()
            val screenState by viewModel.screenState.collectAsStateWithLifecycle()

            WritePostArgument(
                dataState = dataState,
                screenState = screenState,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data: WritePostData by viewModel.writePostData.collectAsStateWithLifecycle()

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            WritePostScreen(
                navController = navController,
                argument = argument,
                data = data,
                onSubmit = { viewModel.submit() }
            )
        }
    }
}
