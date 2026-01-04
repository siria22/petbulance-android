package com.example.presentation.screen.nonfeature.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.utils.nav.ScreenDestinations
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun SplashScreen(
    navController: NavController,
    argument: SplashArgument,
) {
    LaunchedEffect(argument.event) {
        argument.event.collect { event ->
            when (event) {
                is SplashEvent.NavigateToLogin -> {
                    navController.navigate(ScreenDestinations.Login.route) {
                        popUpTo(ScreenDestinations.Splash.route) { inclusive = true }
                    }
                }

                is SplashEvent.NavigateToHome -> {
                    navController.navigate(ScreenDestinations.Home.route) {
                        popUpTo(ScreenDestinations.Splash.route) { inclusive = true }
                    }
                }

                is SplashEvent.NavigateToHomeWithTermsCheck -> {
                    // TODO: 약관 체크 필요함을 Home에 전달하는 방식 고려 (Navigation Argument 등)
                    navController.navigate(ScreenDestinations.Home.route) {
                        popUpTo(ScreenDestinations.Splash.route) { inclusive = true }
                    }
                }

                is SplashEvent.DataFetch.Error -> {

                }
            }
        }
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { }
    }

}

@Preview
@Composable
private fun SplashScreenPreview() {
    PetbulanceTheme {
        SplashScreen(
            navController = rememberNavController(),
            argument = SplashArgument(
                intent = { },
                event = MutableSharedFlow()
            ),
        )
    }
}