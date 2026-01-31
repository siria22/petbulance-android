package com.petbulance.presentation.screen.nonfeature.login.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.petbulance.presentation.component.ui.atom.BasicBottomSheet
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.screen.nonfeature.login.terms.TermsContent
import com.petbulance.presentation.screen.nonfeature.login.terms.TermsData
import com.petbulance.presentation.screen.nonfeature.login.terms.TermsDetailOverlay
import com.petbulance.presentation.screen.nonfeature.login.terms.TermsEvent
import com.petbulance.presentation.screen.nonfeature.login.terms.TermsIntent
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
    var showTermsSheet by rememberSaveable { mutableStateOf(true) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    LaunchedEffect(event) {
        event.collect { e ->
            when (e) {
                is TermsEvent.NavigateToNext -> {
                    showTermsSheet = false
                }

                is TermsEvent.DataFetch.Error -> {
                    // no-op
                }
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

    val onDismissRequest = {
        showTermsSheet = false
    }

    if (showTermsSheet) {
        BasicBottomSheet(
            showBottomSheet = true,
            sheetState = sheetState,
            onDismissRequest = onDismissRequest
        ) {
            TermsContent(
                data = data,
                onIntent = intent,
                onCancel = onDismissRequest,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (data.currentTerm != null) {
            TermsDetailOverlay(
                term = data.currentTerm,
                onDismissRequest = { intent(TermsIntent.OnCloseDetail) }
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