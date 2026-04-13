package com.petbulance.presentation.screen.feature.mypage.sections.help.terms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.domain.model.feature.user.terms.Term
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.CommonDivider
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.atom.OnContentLoadingUi
import com.petbulance.presentation.component.ui.iconSizeMedium
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.organism.TopBarAlignment
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.screen.feature.mypage.sections.help.terms.composables.RequiredTermsWithdrawDialog
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safeNavigate
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun TermsListScreen(
    navController: NavController,
    argument: TermsListArgument,
    data: TermsListData
) {
    val dataState = argument.dataState
    val screenState = argument.screenState
    val isLoading = dataState is TermsListDataState.Loading

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "약관 및 정책",
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
                isLoading && data.termsList.isEmpty() -> {
                    OnContentLoadingUi(text = "약관을 불러오는 중...")
                }

                data.termsList.isEmpty() -> {
                    EmptyTermsState()
                }

                else -> {
                    TermsListContent(
                        termsList = data.termsList,
                        onTermItemClicked = { term ->
                            val termsType = term.termsType?.name ?: return@TermsListContent
                            navController.safeNavigate(
                                ScreenDestinations.MyPage.Help.Terms.Detail.createRoute(termsType)
                            )
                        }
                    )
                }
            }
        }
    }

    when (val state = screenState) {
        is TermsListScreenState.ShowRequiredTermsDialog -> {
            RequiredTermsWithdrawDialog(
                onDismiss = {
                    argument.intent(TermsListIntent.DismissRequiredTermsDialog)
                },
                onWithdrawConfirm = {
                    argument.intent(TermsListIntent.DismissRequiredTermsDialog)
                    navController.safeNavigate(ScreenDestinations.MyPage.User.Withdrawal.route)
                }
            )
        }

        is TermsListScreenState.Init -> {
            // No dialog
        }
    }
}

@Composable
private fun TermsListContent(
    termsList: List<Term>,
    onTermItemClicked: (Term) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        termsList.forEach { term ->
            TermsListItem(
                term = term,
                onItemClicked = { onTermItemClicked(term) }
            )
            CommonDivider()
        }
    }
}

@Composable
private fun TermsListItem(
    term: Term,
    onItemClicked: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClicked() }
            .padding(horizontal = spacingMedium, vertical = spacingSmall)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXS),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = term.title,
                style = typography.bodyLarge,
                color = colorScheme.text.primary
            )
        }
        BasicIcon(
            iconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowRight),
            contentDescription = "약관 상세 보기",
            size = iconSizeMedium,
            tint = colorScheme.icon.veryLight
        )
    }
}

@Composable
private fun EmptyTermsState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "약관 정보를 불러올 수 없습니다",
            style = typography.bodyLarge,
            color = colorScheme.text.secondary
        )
    }
}

@Preview
@Composable
private fun TermsListScreenPreview() {
    PetbulanceTheme {
        TermsListScreen(
            navController = rememberNavController(),
            argument = TermsListArgument(
                intent = { },
                dataState = TermsListDataState.Loaded,
                screenState = TermsListScreenState.Init,
                event = MutableSharedFlow()
            ),
            data = TermsListData.stub()
        )
    }
}

@Preview
@Composable
private fun TermsListScreenLoadingPreview() {
    PetbulanceTheme {
        TermsListScreen(
            navController = rememberNavController(),
            argument = TermsListArgument(
                intent = { },
                dataState = TermsListDataState.Loading,
                screenState = TermsListScreenState.Init,
                event = MutableSharedFlow()
            ),
            data = TermsListData(termsList = emptyList())
        )
    }
}
