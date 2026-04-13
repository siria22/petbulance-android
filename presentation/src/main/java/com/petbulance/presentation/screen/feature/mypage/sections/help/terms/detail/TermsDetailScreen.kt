package com.petbulance.presentation.screen.feature.mypage.sections.help.terms.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.domain.model.feature.user.terms.Term
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.BasicDialog
import com.petbulance.presentation.component.ui.atom.BasicToggleSwitch
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.atom.OnContentLoadingUi
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.organism.TopBarAlignment
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.component.ui.spacingLarge
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.screen.feature.mypage.sections.help.terms.composables.RequiredTermsWithdrawDialog
import com.petbulance.presentation.utils.HtmlText
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safeNavigate
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun TermsDetailScreen(
    navController: NavController,
    argument: TermsDetailArgument,
    data: TermsDetailData
) {
    val dataState = argument.dataState
    val screenState = argument.screenState
    val isLoading = dataState is TermsDetailDataState.Loading

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = data.term?.title ?: "약관 상세",
                    textAlignment = TopBarAlignment.START,
                    isLeadingIconAvailable = true,
                    onLeadingIconClicked = {
                        navController.navigateUp()
                    },
                    leadingIconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
                    isTrailingIconAvailable = false
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
        Box(modifier = Modifier.padding(innerPadding)) {
            when {
                isLoading -> {
                    OnContentLoadingUi(text = "약관을 불러오는 중...")
                }

                data.term == null -> {
                    EmptyTermsDetailState()
                }

                else -> {
                    TermsDetailContent(
                        term = data.term,
                        isAgreed = data.isAgreed,
                        isToggleEnabled = data.term.content.isNotBlank(),
                        onToggleChanged = { isAgreed ->
                            argument.intent(TermsDetailIntent.OnToggleChanged(isAgreed))
                        }
                    )
                }
            }
        }
    }

    when (val state = screenState) {
        is TermsDetailScreenState.ShowRequiredTermsDialog -> {
            RequiredTermsWithdrawDialog(
                onDismiss = {
                    argument.intent(TermsDetailIntent.DismissRequiredTermsDialog)
                },
                onWithdrawConfirm = {
                    argument.intent(TermsDetailIntent.DismissRequiredTermsDialog)
                    navController.safeNavigate(ScreenDestinations.MyPage.User.Withdrawal.route)
                }
            )
        }

        is TermsDetailScreenState.ShowContentLoadFailedDialog -> {
            ContentLoadFailedDialog(
                onDismiss = {
                    argument.intent(TermsDetailIntent.DismissContentLoadFailedDialog)
                    navController.navigateUp()
                }
            )
        }

        is TermsDetailScreenState.Init -> {
            // No dialog
        }
    }
}

@Composable
private fun TermsDetailContent(
    term: Term,
    isAgreed: Boolean,
    isToggleEnabled: Boolean = true,
    onToggleChanged: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = spacingMedium, vertical = spacingXL)
    ) {
        HtmlText(
            html = term.content,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = spacingXL)
        ) {
            Text(
                text = if (term.required) "(필수) 서비스 이용약관 철회하기" else "(선택) 서비스 이용약관 철회하기",
                style = typography.bodyMedium,
                color = colorScheme.text.tertiary
            )
            BasicToggleSwitch(
                checked = isAgreed,
                onCheckedChange = if (isToggleEnabled) onToggleChanged else {
                    {}
                }
            )
        }
    }
}

@Composable
private fun EmptyTermsDetailState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "약관 내용을 불러올 수 없습니다",
            style = typography.bodyLarge,
            color = colorScheme.text.secondary
        )
    }
}

@Composable
private fun ContentLoadFailedDialog(
    onDismiss: () -> Unit,
) {
    BasicDialog(
        backHandler = onDismiss,
        paddingValues = PaddingValues(horizontal = 16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(spacingXS),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "약관을 불러오지 못했어요.",
                    style = typography.titleMedium.emp(),
                    textAlign = TextAlign.Center,
                    color = colorScheme.text.primary
                )
                Text(
                    text = "약관 내용을 불러올 수 없습니다.\n네트워크 연결을 확인하고 다시 시도해주세요.",
                    style = typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = colorScheme.text.secondary
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingXS),
                modifier = Modifier.fillMaxWidth()
            ) {
                BasicButton(
                    modifier = Modifier.weight(3f),
                    text = "돌아가기",
                    size = BasicButtonSize.L,
                    buttonType = BasicButtonType.SECONDARY,
                    radius = 12.dp,
                    onClicked = onDismiss
                )
            }
        }
    }
}

@Preview
@Composable
private fun TermsDetailScreenPreview() {
    PetbulanceTheme {
        TermsDetailScreen(
            navController = rememberNavController(),
            argument = TermsDetailArgument(
                intent = { },
                dataState = TermsDetailDataState.Loaded,
                screenState = TermsDetailScreenState.Init,
                event = MutableSharedFlow()
            ),
            data = TermsDetailData.stub()
        )
    }
}

@Preview
@Composable
private fun TermsDetailScreenLoadingPreview() {
    PetbulanceTheme {
        TermsDetailScreen(
            navController = rememberNavController(),
            argument = TermsDetailArgument(
                intent = { },
                dataState = TermsDetailDataState.Loading,
                screenState = TermsDetailScreenState.Init,
                event = MutableSharedFlow()
            ),
            data = TermsDetailData(term = null, isAgreed = false)
        )
    }
}
