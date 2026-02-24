package com.petbulance.presentation.screen.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.screen.nonfeature.login.terms.TermsData
import com.petbulance.presentation.utils.SectionLoadState
import kotlinx.coroutines.flow.MutableSharedFlow

@Preview(name = "홈 화면 - 로딩 상태")
@Composable
private fun HomeScreenLoadingPreview() {
    PetbulanceTheme {
        HomeScreen(
            navController = rememberNavController(),
            argument = HomeArgument(
                intent = { },
                reviewState = SectionLoadState.Loading,
                bannerState = SectionLoadState.Loading,
                hotArticleState = SectionLoadState.Loading,
                dataState = HomeDataState.Init,
                screenState = HomeScreenState.Init,
                event = MutableSharedFlow()
            ),
            data = HomeData.stub,
            checkTermsInitialState = false,
            termsData = TermsData.stub(),
            termsIntent = { },
            termsEvent = MutableSharedFlow()
        )
    }
}

@Preview(name = "홈 화면 - 에러 상태")
@Composable
private fun HomeScreenErrorPreview() {
    PetbulanceTheme {
        HomeScreen(
            navController = rememberNavController(),
            argument = HomeArgument(
                intent = { },
                reviewState = SectionLoadState.Error("최근 리뷰를 불러올 수 없습니다"),
                bannerState = SectionLoadState.Error("배너를 불러올 수 없습니다"),
                hotArticleState = SectionLoadState.Error("인기 게시글을 불러올 수 없습니다"),
                dataState = HomeDataState.Init,
                screenState = HomeScreenState.Init,
                event = MutableSharedFlow()
            ),
            data = HomeData(
                recentReviews = emptyList(),
                hotArticles = null,
                homeBanners = emptyList()
            ),
            checkTermsInitialState = false,
            termsData = TermsData.stub(),
            termsIntent = { },
            termsEvent = MutableSharedFlow()
        )
    }
}

@Preview(name = "홈 화면 - 정상 상태")
@Composable
private fun HomeScreenSuccessPreview() {
    PetbulanceTheme {
        HomeScreen(
            navController = rememberNavController(),
            argument = HomeArgument(
                intent = { },
                reviewState = SectionLoadState.Success,
                bannerState = SectionLoadState.Success,
                hotArticleState = SectionLoadState.Success,
                dataState = HomeDataState.Init,
                screenState = HomeScreenState.Init,
                event = MutableSharedFlow()
            ),
            data = HomeData.stub,
            checkTermsInitialState = false,
            termsData = TermsData.stub(),
            termsIntent = { },
            termsEvent = MutableSharedFlow()
        )
    }
}

@Preview(name = "홈 화면 - Coming Soon (인기 게시글)")
@Composable
private fun HomeScreenComingSoonPreview() {
    PetbulanceTheme {
        HomeScreen(
            navController = rememberNavController(),
            argument = HomeArgument(
                intent = { },
                reviewState = SectionLoadState.Success,
                bannerState = SectionLoadState.Success,
                hotArticleState = SectionLoadState.Success,
                dataState = HomeDataState.Init,
                screenState = HomeScreenState.Init,
                event = MutableSharedFlow()
            ),
            data = HomeData.stub,
            checkTermsInitialState = false,
            termsData = TermsData.stub(),
            termsIntent = { },
            termsEvent = MutableSharedFlow()
        )
    }
}
