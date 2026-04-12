package com.petbulance.petbulance.common.nav

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.petbulance.presentation.analytics.AnalyticsEvents
import com.petbulance.presentation.analytics.AnalyticsTracker
import com.petbulance.presentation.analytics.ScreenNames
import com.petbulance.presentation.screen.feature.community.communityDestination
import com.petbulance.presentation.screen.feature.community.detail.postDetailDestination
import com.petbulance.presentation.screen.feature.community.write.writePostDestination
import com.petbulance.presentation.screen.feature.home.homeDestination
import com.petbulance.presentation.screen.feature.mypage.main.myPageDestination
import com.petbulance.presentation.screen.feature.mypage.sections.activity.comments.myPageCommentsDestination
import com.petbulance.presentation.screen.feature.mypage.sections.activity.posts.myPagePostsDestination
import com.petbulance.presentation.screen.feature.mypage.sections.activity.reviews.myPageReviewsDestination
import com.petbulance.presentation.screen.feature.mypage.sections.help.cs.cSDestination
import com.petbulance.presentation.screen.feature.mypage.sections.help.cs.coalition.coalitionDestination
import com.petbulance.presentation.screen.feature.mypage.sections.help.cs.qna.create.qnaCreateDestination
import com.petbulance.presentation.screen.feature.mypage.sections.help.cs.qna.detail.qnaDetailDestination
import com.petbulance.presentation.screen.feature.mypage.sections.help.cs.qna.list.qnaListDestination
import com.petbulance.presentation.screen.feature.mypage.sections.help.notice.detail.myPageNoticeDetailDestination
import com.petbulance.presentation.screen.feature.mypage.sections.help.notice.list.myPageNoticeDestination
import com.petbulance.presentation.screen.feature.mypage.sections.help.terms.termsListDestination
import com.petbulance.presentation.screen.feature.mypage.sections.help.terms.detail.termsDetailDestination
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

@RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    analyticsTracker: AnalyticsTracker
) {
    val navController = rememberNavController()

    // 화면 추적 + 탭 전환 이벤트
    var previousScreenName by remember { mutableStateOf<String?>(null) }

    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            val screenName = ScreenNames.fromRoute(destination.route) ?: return@OnDestinationChangedListener

            // screen_view 자동 추적
            analyticsTracker.trackScreen(screenName)

            // view_tab 이벤트: 하단 탭 화면 전환 시
            if (ScreenNames.isTabScreen(screenName)) {
                val tabName = ScreenNames.toTabName(screenName)
                val prevTab = previousScreenName?.let { ScreenNames.toTabName(it) }
                if (tabName != null) {
                    analyticsTracker.trackEvent(
                        AnalyticsEvents.VIEW_TAB,
                        buildMap {
                            put(AnalyticsEvents.Params.TAB_NAME, tabName)
                            if (prevTab != null) put(AnalyticsEvents.Params.PREVIOUS_TAB, prevTab)
                        }
                    )
                }
            }

            previousScreenName = screenName
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose { navController.removeOnDestinationChangedListener(listener) }
    }

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
        myPagePostsDestination(navController = navController)
        myPageCommentsDestination(navController = navController)
        myPageAccountDestination(navController = navController)

        myPageNoticeDestination(navController = navController)
        myPageNoticeDetailDestination(navController = navController)

        cSDestination(navController = navController)
        coalitionDestination(navController = navController)
        qnaListDestination(navController = navController)
        qnaCreateDestination(navController = navController)
        qnaDetailDestination(navController = navController)

        termsListDestination(navController = navController)
        termsDetailDestination(navController = navController)

        communityDestination(navController = navController)
        postDetailDestination(navController = navController)
        writePostDestination(navController = navController)
    }
}