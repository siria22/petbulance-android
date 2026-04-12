package com.petbulance.presentation.screen.feature.community.detail

import android.os.Build
import androidx.annotation.RequiresExtension
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

@RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
fun NavGraphBuilder.postDetailDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Community.PostDetail.route,
        arguments = listOf(
            navArgument(name = ScreenDestinations.Community.PostDetail.ARG_ID) {
                type = NavType.LongType
                defaultValue = 0L
            }
        )
    ) {
        val viewModel: PostDetailViewModel = hiltViewModel()

        val argument: PostDetailArgument = let {
            val dataState by viewModel.dataState.collectAsStateWithLifecycle()
            val screenState by viewModel.screenState.collectAsStateWithLifecycle()

            PostDetailArgument(
                dataState = dataState,
                screenState = screenState,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data: PostDetailData = let {
            val postDetailData by viewModel.postDetailData.collectAsStateWithLifecycle()
            postDetailData
        }

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            PostDetailScreen(
                navController = navController,
                argument = argument,
                data = data
            )
        }
    }
}
