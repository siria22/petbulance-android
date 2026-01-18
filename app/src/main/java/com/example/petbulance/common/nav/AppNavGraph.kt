package com.example.petbulance.common.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.presentation.screen.feature.home.homeDestination
import com.example.presentation.screen.feature.review.main.reviewDestination
import com.example.presentation.screen.feature.review.search.reviewSearchDestination
import com.example.presentation.screen.feature.search.info.hospitalInfoDestination
import com.example.presentation.screen.feature.search.main.searchDestination
import com.example.presentation.screen.nonfeature.login.main.loginDestination
import com.example.presentation.screen.nonfeature.login.terms.termsDestination
import com.example.presentation.screen.nonfeature.splash.splashDestination
import com.example.presentation.utils.nav.ScreenDestinations

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
    }
}