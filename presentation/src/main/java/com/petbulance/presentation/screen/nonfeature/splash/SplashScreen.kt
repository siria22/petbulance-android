package com.petbulance.presentation.screen.nonfeature.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.presentation.component.theme.PetbulancePrimitives
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.atom.CustomGreenLoader
import com.petbulance.presentation.utils.error.ErrorDialog
import com.petbulance.presentation.utils.error.ErrorDialogState
import com.petbulance.presentation.utils.nav.ScreenDestinations
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun SplashScreen(
    navController: NavController,
    argument: SplashArgument,
) {

    var isError by remember { mutableStateOf(false) }
    var customErrorDialogState by remember { mutableStateOf(ErrorDialogState.idle()) }

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
                    customErrorDialogState = ErrorDialogState(
                        userMessage = event.userMessage,
                        exceptionMessage = event.exceptionMessage,
                        isErrorDialogVisible = true
                    )
                    isError = true
                }
            }
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(color = PetbulancePrimitives.Base.black)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CustomGreenLoader(size = 48.dp)
            Text(
                text = "잠시만 기다려주세요...",
                style = typography.titleSmall.emp(),
                color = colorScheme.text.caption
            )
        }
    }

    if (isError) {
        ErrorDialog(
            errorDialogState = customErrorDialogState,
        ) {
            isError = false
            navController.navigate(ScreenDestinations.Login.route) {
                popUpTo(ScreenDestinations.Splash.route) { inclusive = true }
            }
        }
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