package com.petbulance.petbulance.common.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.petbulance.presentation.screen.feature.home.homeDestination
import com.petbulance.presentation.screen.feature.review.create.reviewCreateDestination
import com.petbulance.presentation.screen.feature.review.main.reviewDestination
import com.petbulance.presentation.screen.feature.review.search.reviewSearchDestination
import com.petbulance.presentation.screen.feature.search.info.hospitalInfoDestination
import com.petbulance.presentation.screen.feature.search.main.searchDestination
import com.petbulance.presentation.screen.nonfeature.login.main.loginDestination
import com.petbulance.presentation.screen.nonfeature.login.terms.termsDestination
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
        termsDestination(navController = navController)

        homeDestination(navController = navController)

        searchDestination(navController = navController)
        hospitalInfoDestination(navController = navController)

        reviewDestination(navController = navController)
        reviewSearchDestination(navController = navController)
        reviewCreateDestination(navController = navController)
    }
}