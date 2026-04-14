package com.petbulance.presentation.screen.feature.mypage.sections.user.withdrawal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.petbulance.presentation.component.theme.PetbulanceTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.molecule.WarningDialog
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.organism.TopBarAlignment
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safePopBackStack

@Composable
fun WithdrawalScreen(
    navController: NavController,
    argument: WithdrawalArgument
) {
    var showConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(argument.event) {
        argument.event.collect { event ->
            when (event) {
                is WithdrawalEvent.WithdrawalSuccess -> {
                    navController.navigate(ScreenDestinations.Login.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "",
                    textAlignment = TopBarAlignment.START,
                    isLeadingIconAvailable = true,
                    onLeadingIconClicked = { navController.safePopBackStack() },
                    isTrailingIconAvailable = false,
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = CurrentBottomNav.MY,
                navController = navController
            )
        },
        containerColor = colorScheme.bg.frame.default
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacingMedium, vertical = spacingXL),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "펫뷸런스 서비스",
                    style = typography.bodyLarge,
                    color = colorScheme.text.primary
                )
                Text(
                    text = "탈퇴하기",
                    style = typography.bodySmall.emp(),
                    color = colorScheme.text.caption,
                    modifier = Modifier.clickable { showConfirmDialog = true }
                )
            }
        }
    }

    if (showConfirmDialog) {
        WarningDialog(
            title = "정말 탈퇴하시겠습니까?",
            content = "탈퇴하기 버튼을 누르면 즉시 탈퇴 처리 됩니다.",
            cancelText = "돌아가기",
            confirmText = "탈퇴하기",
            onDismissRequest = { showConfirmDialog = false },
            onExitButtonClicked = {
                showConfirmDialog = false
                argument.intent(WithdrawalIntent.ConfirmWithdrawal)
            }
        )
    }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
private fun WithdrawalScreenPreview() {
    PetbulanceTheme {
        WithdrawalScreen(
            navController = rememberNavController(),
            argument = WithdrawalArgument(
                intent = {},
                dataState = WithdrawalDataState.Init,
                event = MutableSharedFlow()
            )
        )
    }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
private fun WithdrawalScreenWithDialogPreview() {
    PetbulanceTheme {
        // Dialog는 상태로 제어되므로 별도로 미리보기
        WarningDialog(
            title = "정말 탈퇴하시겠습니까?",
            content = "탈퇴하기 버튼을 누르면 즉시 탈퇴 처리 됩니다.",
            cancelText = "돌아가기",
            confirmText = "탈퇴하기",
            onDismissRequest = {},
            onExitButtonClicked = {}
        )
    }
}
