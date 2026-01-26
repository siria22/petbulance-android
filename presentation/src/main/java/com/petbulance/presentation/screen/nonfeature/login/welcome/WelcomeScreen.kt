package com.petbulance.presentation.screen.nonfeature.login.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.atom.*
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.screen.nonfeature.login.terms.*
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safeNavigate
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    navController: NavController,
    data: TermsData,
    intent: (TermsIntent) -> Unit,
    event: SharedFlow<TermsEvent>
) {
    var showSheet by rememberSaveable { mutableStateOf(true) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    LaunchedEffect(event) {
        event.collect { evt ->
            if (evt is TermsEvent.NavigateToNext) {
                showSheet = false
            }
        }
    }

    Scaffold(
        containerColor = colorScheme.bg.frame.default,
        bottomBar = {
            BasicButton(
                text = "시작하기",
                size = BasicButtonSize.L,
                buttonType = BasicButtonType.PRIMARY,
                onClicked = {
                    navController.safeNavigate(ScreenDestinations.Home.route) {
                        popUpTo(ScreenDestinations.Login.route) { inclusive = true }
                    }
                },
                radius = 16.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacingMedium, vertical = 56.dp)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            WelcomeScreenContent(
                tempUserName = data.userTempName
            )
        }
    }

    // 약관 동의 바텀시트
    if (showSheet) {
        BasicBottomSheet(
            showBottomSheet = true,
            sheetState = sheetState,
            onDismissRequest = {
                // TODO : 정책: 약관 동의 없이는 진입 불가하므로, 닫기 시 특별한 동작 없음(화면에 머무름)
                // 혹은 showSheet = false 처리하여 "시작하기" 버튼을 노출시킬지 결정 필요
                showSheet = false
            }
        ) {
            TermsContent(
                data = data,
                onIntent = intent,
                onCancel = { showSheet = false },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun WelcomeScreenContent(tempUserName: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = spacingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacingMedium, Alignment.CenterVertically)
    ) {
        // TODO: 디자인에 맞는 폭죽 이미지 리소스로 교체 필요 (R.drawable.img_welcome 등)
        Text(text = "🎉", fontSize = 80.sp)

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = colorScheme.action.primary.default)) {
                    append(tempUserName)
                }
                append("님, 환영해요!")
            },
            style = typography.titleLarge.emp(),
            color = colorScheme.text.primary,
            textAlign = TextAlign.Center
        )
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = colorScheme.action.primary.default)) {
                    append("펫뷸런스")
                }
                append("에서 지금 필요한\n병원 정보를 검색하세요")
            },
            style = typography.bodyLarge,
            color = colorScheme.text.secondary,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(apiLevel = 34)
@Composable
private fun WelcomeScreenPreview() {
    PetbulanceTheme {
        WelcomeScreen(
            navController = rememberNavController(),
            data = TermsData.stub(),
            intent = {},
            event = MutableSharedFlow()
        )
    }
}