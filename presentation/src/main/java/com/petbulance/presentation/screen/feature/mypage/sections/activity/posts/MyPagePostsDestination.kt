package com.petbulance.presentation.screen.feature.mypage.sections.activity.posts

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.myPagePostsDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.MyPage.Activity.Posts.route
    ) {
        val viewModel: MyPagePostsViewModel = hiltViewModel()

        val argument: MyPagePostsArgument = let {
            val dataState by viewModel.dataState.collectAsStateWithLifecycle()
            val screenState by viewModel.screenState.collectAsStateWithLifecycle()

            MyPagePostsArgument(
                dataState = dataState,
                screenState = screenState,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        // TODO : 실제 data로 변경
        val data: MyPagePostsData = MyPagePostsData.empty

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            MyPagePostsScreen(
                navController = navController,
                argument = argument,
                data = data
            )
        }
    }
}
