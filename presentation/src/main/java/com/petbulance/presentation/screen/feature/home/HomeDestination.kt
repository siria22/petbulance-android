package com.petbulance.presentation.screen.feature.home

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.petbulance.presentation.screen.nonfeature.login.terms.TermsData
import com.petbulance.presentation.screen.nonfeature.login.terms.TermsViewModel
import com.petbulance.presentation.utils.CommonScreenWrapper
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.homeDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Home.route,
        // argument 정의 추가 필요 (navArgument)
        arguments = listOf(
            navArgument(ScreenDestinations.Home.ARG_CHECK_TERMS) {
                defaultValue = false
                type = NavType.BoolType
            }
        )
    ) { entry ->
        val viewModel: HomeViewModel = hiltViewModel()

        val termsViewModel: TermsViewModel = hiltViewModel()
        val termsDataState by termsViewModel.dataState.collectAsStateWithLifecycle()
        val termsScreenState by termsViewModel.screenState.collectAsStateWithLifecycle()
        val termsList by termsViewModel.termsList.collectAsStateWithLifecycle()
        val agreedTermIds by termsViewModel.agreedTermIds.collectAsStateWithLifecycle()
        val isAllRequiredAgreed by termsViewModel.isAllRequiredAgreed.collectAsStateWithLifecycle()
        val currentTerm by termsViewModel.currentTerm.collectAsStateWithLifecycle()
        val userTempName by termsViewModel.userTempName.collectAsStateWithLifecycle()

        val termsData = TermsData(
            termsList = termsList,
            agreedTermIds = agreedTermIds,
            isAllRequiredAgreed = isAllRequiredAgreed,
            currentTerm = currentTerm,
            userTempName = userTempName
        )

        val checkTerms = entry.arguments?.getBoolean(ScreenDestinations.Home.ARG_CHECK_TERMS) ?: false

        val argument: HomeArgument = let {
            val dataState by viewModel.dataState.collectAsStateWithLifecycle()
            val screenState by viewModel.screenState.collectAsStateWithLifecycle()
            val reviewState by viewModel.reviewState.collectAsStateWithLifecycle()
            val bannerState by viewModel.bannerState.collectAsStateWithLifecycle()
            val hotArticleState by viewModel.hotArticleState.collectAsStateWithLifecycle()

            HomeArgument(
                dataState = dataState,
                screenState = screenState,
                reviewState = reviewState,
                bannerState = bannerState,
                hotArticleState = hotArticleState,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data: HomeData = let {
            val recentReviews by viewModel.recentReviews.collectAsStateWithLifecycle()
            val hotArticle by viewModel.hotArticles.collectAsStateWithLifecycle()
            val homeBanners by viewModel.homeBanners.collectAsStateWithLifecycle()

            HomeData(
                recentReviews = recentReviews,
                hotArticles = hotArticle,
                homeBanners = homeBanners
            )
        }

        val errorState by viewModel.errorDialogState.collectAsStateWithLifecycle()

        CommonScreenWrapper(
            errorState = errorState,
            dismissErrorDialog = viewModel::dismissErrorDialog,
        ) {
            HomeScreen(
                navController = navController,
                argument = argument,
                data = data,
                checkTermsInitialState = checkTerms,
                termsData = termsData,
                termsIntent = termsViewModel::onIntent,
                termsEvent = termsViewModel.event
            )
        }
    }
}