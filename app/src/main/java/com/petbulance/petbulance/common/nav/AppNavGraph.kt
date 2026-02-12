package com.petbulance.petbulance.common.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.petbulance.presentation.screen.feature.home.homeDestination
import com.petbulance.presentation.screen.feature.mypage.main.myPageDestination
import com.petbulance.presentation.screen.feature.mypage.sections.activity.reviews.myPageReviewsDestination
import com.petbulance.presentation.screen.feature.mypage.sections.help.notice.detail.myPageNoticeDetailDestination
import com.petbulance.presentation.screen.feature.mypage.sections.help.notice.list.myPageNoticeDestination
import com.petbulance.presentation.screen.feature.mypage.sections.user.account.myPageAccountDestination
import com.petbulance.presentation.screen.feature.mypage.sections.user.profile.myPageProfileDestination
import com.petbulance.presentation.screen.feature.review.camera.receiptCameraDestination
import com.petbulance.presentation.screen.feature.review.create.reviewCreateDestination
import com.petbulance.presentation.screen.feature.review.detail.reviewDetailDestination
import com.petbulance.presentation.screen.feature.review.edit.reviewEditDestination
import com.petbulance.presentation.screen.feature.review.main.reviewDestination
import com.petbulance.presentation.screen.feature.review.search.reviewSearchDestination
import com.petbulance.presentation.screen.feature.search.info.hospitalInfoDestination
import com.petbulance.presentation.screen.feature.search.main.searchDestination
import com.petbulance.presentation.screen.nonfeature.login.main.loginDestination
import com.petbulance.presentation.screen.nonfeature.login.welcome.welcomeDestination
import com.petbulance.presentation.screen.nonfeature.splash.splashDestination
import com.petbulance.presentation.utils.nav.ScreenDestinations

@Composable
fun AppNavGraph(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ScreenDestinations.Splash.route,
        modifier = modifier
    ) {
        splashDestination(navController = navController)
        loginDestination(navController = navController)
        welcomeDestination(navController = navController)

        homeDestination(navController = navController)

        searchDestination(navController = navController)
        hospitalInfoDestination(navController = navController)

        reviewDestination(navController = navController)
        reviewSearchDestination(navController = navController)
        reviewCreateDestination(navController = navController)
        receiptCameraDestination(navController = navController)
        reviewEditDestination(navController = navController)
        reviewDetailDestination(navController = navController)

        myPageDestination(navController = navController)

        myPageProfileDestination(navController = navController)
        myPageReviewsDestination(navController = navController)
        myPageAccountDestination(navController = navController)

        myPageNoticeDestination(navController = navController)
        myPageNoticeDetailDestination(navController = navController)
    }
}