package com.petbulance.presentation.screen.nonfeature.login.main

import android.Manifest
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.domain.model.type.LoginProviderType
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulancePrimitives
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.color.ColorObject
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.DefaultRoundedCorner
import com.petbulance.presentation.component.ui.atom.CustomGreenLoader
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXXL
import com.petbulance.presentation.utils.hooks.HandleMultiplePermissions
import com.petbulance.presentation.utils.hooks.login.rememberGoogleLoginManager
import com.petbulance.presentation.utils.hooks.login.rememberKakaoLoginManager
import com.petbulance.presentation.utils.hooks.login.rememberNaverLoginManager
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safeNavigate
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun LoginScreen(
    navController: NavController,
    argument: LoginArgument,
) {
    val dataState = argument.dataState

    val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_MEDIA_IMAGES
        )
    } else {
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    HandleMultiplePermissions(
        permissions = permissionsToRequest,
        onAllGranted = { /* 모든 권한 허용됨 - 필요 시 로깅 */ },
        onDenied = { /* 일부 권한 거부됨 - 로그인은 계속 진행 */ }
    )

    LaunchedEffect(argument.event) {
        argument.event.collect { event ->
            when (event) {
                is LoginEvent.NavigateToHome -> {
                    navController.safeNavigate(ScreenDestinations.Home.route) {
                        popUpTo(ScreenDestinations.Login.route) { inclusive = true }
                    }
                }

                is LoginEvent.NavigateToTerms -> {
                    navController.safeNavigate(ScreenDestinations.Terms.route)
                }

                else -> {}
            }
        }
    }
    Scaffold(
        containerColor = colorScheme.bg.frame.default
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            LoginScreenContents(
                onIntent = argument.intent,
                onNavigateToHome = {
                    navController.safeNavigate(ScreenDestinations.Home.route) {
                        popUpTo(ScreenDestinations.Login.route) { inclusive = true }
                    }
                }
            )

            if (dataState is LoginDataState.Loading) {
                CustomGreenLoader()
            }
        }
    }

    // BackHandler {  }
}

@Composable
private fun LoginScreenContents(
    onIntent: (LoginIntent) -> Unit,
    onNavigateToHome: () -> Unit,
) {
    val loginWithKakao = rememberKakaoLoginManager { token ->
        if (token != null) {
            onIntent(LoginIntent.OnSocialLogin(LoginProviderType.KAKAO, token))
        }
    }

    val loginWithNaver = rememberNaverLoginManager { token ->
        if (token != null) {
            onIntent(LoginIntent.OnSocialLogin(LoginProviderType.NAVER, token))
        }
    }

    val loginWithGoogle = rememberGoogleLoginManager { token ->
        if (token != null) {
            onIntent(LoginIntent.OnSocialLogin(LoginProviderType.GOOGLE, token))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .padding(vertical = spacingXXL)
                .size(140.dp),
            painter = painterResource(R.drawable.logo_main),
            contentDescription = "Main Logo"
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(spacingSmall),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = spacingMedium)
        ) {
            Text(
                text = "검증된 특수동물 병원 정보부터\n집사들의 실생활 케어 꿀팁까지,",
                style = typography.bodyLarge,
                color = colorScheme.text.tertiary
            )

            Text(
                text = buildAnnotatedString {
                    append("지금 ")

                    withStyle(
                        style = SpanStyle(color = PetbulancePrimitives.Primary.p500)
                    ) {
                        append("펫불런스")
                    }

                    append("에서 시작하세요!")
                },
                color = colorScheme.text.primary,
                style = typography.titleLarge.emp()
            )
        }

        LoginButtonColumn(
            onGoogleLoginButtonClicked = loginWithGoogle,
            onKakaoLoginButtonClicked = loginWithKakao,
            onNaverLoginButtonClicked = loginWithNaver,
            onWithoutLoginButtonClicked = onNavigateToHome
        )
    }
}

@Composable
private fun LoginButtonColumn(
    onGoogleLoginButtonClicked: () -> Unit,
    onKakaoLoginButtonClicked: () -> Unit,
    onNaverLoginButtonClicked: () -> Unit,
    onWithoutLoginButtonClicked: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = spacingMedium, vertical = 40.dp)
    ) {
        LoginButton(
            provider = LoginProviderType.KAKAO,
            handler = onKakaoLoginButtonClicked
        )
        LoginButton(
            provider = LoginProviderType.NAVER,
            handler = onNaverLoginButtonClicked
        )
        LoginButton(
            provider = LoginProviderType.GOOGLE,
            iconSize = 28.dp,
            handler = onGoogleLoginButtonClicked
        )
        WithoutLoginButton(
            handler = onWithoutLoginButtonClicked
        )
        /* TODO : 최근에 ~로 로그인 했어요 */
    }
}

@Composable
private fun LoginButton(provider: LoginProviderType, iconSize: Dp = 20.dp, handler: () -> Unit) {

    val iconRes = when (provider) {
        LoginProviderType.GOOGLE -> R.drawable.login_google
        LoginProviderType.KAKAO -> R.drawable.login_kakao
        LoginProviderType.NAVER -> R.drawable.login_naver
    }

    val textColor = when (provider) {
        LoginProviderType.GOOGLE -> Color.Black
        LoginProviderType.KAKAO -> Color.Black
        LoginProviderType.NAVER -> Color.White
    }

    val backgroundColor = when (provider) {
        LoginProviderType.GOOGLE -> ColorObject.Provider.GOOGLE
        LoginProviderType.KAKAO -> ColorObject.Provider.KAKAO
        LoginProviderType.NAVER -> ColorObject.Provider.NAVER
    }

    val googleButtonBorder =
        if (provider == LoginProviderType.GOOGLE) Color(0xFFD7D7D7) else Color.Transparent

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .border(width = 1.dp, color = googleButtonBorder, shape = DefaultRoundedCorner)
            .background(color = backgroundColor, shape = DefaultRoundedCorner)
            .clickable { handler() }
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Image(
            modifier = Modifier.size(iconSize),
            painter = painterResource(iconRes),
            contentDescription = "Log in with $provider"
        )
        Text(
            text = provider.korean + "로 시작하기",
            color = textColor,
            style = typography.bodyLarge.emp(),
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
private fun WithoutLoginButton(
    handler: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .border(
                width = 1.dp,
                color = colorScheme.action.primary.default,
                shape = DefaultRoundedCorner
            )
            .background(color = colorScheme.bg.frame.default, shape = DefaultRoundedCorner)
            .clickable { handler() }
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = "로그인 없이 둘러보기",
            color = colorScheme.action.primary.default,
            style = typography.bodyLarge.emp(),
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    PetbulanceTheme {
        LoginScreen(
            navController = rememberNavController(),
            argument = LoginArgument(
                intent = { },
                dataState = LoginDataState.Init,
                screenState = LoginScreenState.Init,
                event = MutableSharedFlow()
            ),
        )
    }
}