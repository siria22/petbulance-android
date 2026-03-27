package com.petbulance.presentation.screen.feature.mypage.sections.help.cs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.petbulance.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.cSDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.MyPage.Help.CS.route,
    ) {
        CSScreen(navController = navController)
    }
}
