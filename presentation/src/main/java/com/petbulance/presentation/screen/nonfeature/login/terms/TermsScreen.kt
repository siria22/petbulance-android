@file:OptIn(ExperimentalMaterial3Api::class)

package com.petbulance.presentation.screen.nonfeature.login.terms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.domain.model.feature.user.terms.Term
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulancePrimitives
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.atom.BasicBottomSheet
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeMS
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXL
import com.petbulance.presentation.utils.HtmlText
import kotlinx.coroutines.flow.MutableSharedFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsScreen(
    navController: NavController,
    argument: TermsArgument,
    data: TermsData
) {
    LaunchedEffect(argument.event) {
        argument.event.collect { event ->
            when (event) {
                is TermsEvent.NavigateToNext -> {
                    // 다음 화면(닉네임 입력 등)으로 이동
                    // navController.safeNavigate(ScreenDestinations.Nickname.route)
                }

                is TermsEvent.DataFetch.Error -> {
                    // BaseViewModel 처리
                }
            }
        }
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    BasicBottomSheet(
        showBottomSheet = true,
        sheetState = sheetState, // state 주입
        onDismissRequest = { /* 뒤로가기 처리 */ }
    ) {
        TermsScreenContents(
            data = data,
            onIntent = argument.intent
        )
    }
}

@Composable
private fun TermsScreenContents(
    data: TermsData,
    onIntent: (TermsIntent) -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TermsColumn(
                termsList = data.termsList,
                agreedTermIds = data.agreedTermIds,
                isAllRequiredAgreed = data.isAllRequiredAgreed, // 필수 약관 동의 여부 전달
                onDetailVisible = { term ->
                    onIntent(TermsIntent.OnDetailClick(term.id))
                },
                onToggle = { termId ->
                    onIntent(TermsIntent.OnToggleTerm(termId))
                },
                onAgree = { // 동의 버튼 클릭 이벤트 전달
                    onIntent(TermsIntent.OnAgreeClick)
                }
            )
        }

        if (data.currentTerm != null) {
            Column(
                modifier = Modifier
                    .matchParentSize()
                    .background(colorScheme.bg.frame.default)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = spacingXXL, horizontal = spacingMedium)
                    .clickable { onIntent(TermsIntent.OnCloseDetail) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HtmlText(html = data.currentTerm.content)
            }
        }
    }
}

@Composable
private fun TermsColumn(
    termsList: List<Term>,
    agreedTermIds: Set<Long>,
    isAllRequiredAgreed: Boolean, // 추가
    onDetailVisible: (Term) -> Unit,
    onToggle: (Long) -> Unit,
    onAgree: () -> Unit // 추가
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = spacingSmall)
    ) {
        Text(
            text = "펫뷸런스를 쓰려면 동의가 필요해요",
            style = typography.titleSmall,
            color = colorScheme.text.primary,
            modifier = Modifier.padding(horizontal = spacingMedium, vertical = spacingSmall)
        )

        termsList.forEach { term ->
            TermItem(
                title = term.title,
                isRequired = term.required,
                isAgreed = agreedTermIds.contains(term.id),
                onToggle = { onToggle(term.id) },
                onDetailVisible = { onDetailVisible(term) }
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp)
        ) {
            BasicButton(
                modifier = Modifier.fillMaxWidth(),
                text = "동의하기",
                size = BasicButtonSize.L,
                buttonType = if (isAllRequiredAgreed) BasicButtonType.PRIMARY else BasicButtonType.DISABLED,
                radius = 12.dp,
                onClicked = {
                    if (isAllRequiredAgreed) {
                        onAgree()
                    }
                }
            )
            Text(
                text = "닫기",
                style = typography.bodyLarge.emp(),
                color = colorScheme.text.caption,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun TermItem(
    title: String,
    isRequired: Boolean,
    isAgreed: Boolean,
    onToggle: () -> Unit,
    onDetailVisible: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(horizontal = spacingMedium, vertical = spacingXS),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingXS),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            BasicIcon(
                iconResource = IconResource.Drawable(R.drawable.ic_bottomsheet_checked),
                contentDescription = "term checked",
                size = iconSizeMS,
                tint = if (isAgreed) colorScheme.action.link.default else PetbulancePrimitives.Gray.p300
            )

            val badge = if (isRequired) "[필수] " else "[선택] "
            Text(
                text = "$badge$title",
                style = typography.bodySmall.emp(),
                color = colorScheme.text.tertiary
            )
        }

        Text(
            text = "자세히",
            style = typography.labelMedium,
            color = colorScheme.text.caption,
            modifier = Modifier
                .clickable { onDetailVisible() }
                .padding(start = spacingSmall)
        )
    }
}

@Preview(apiLevel = 34)
@Composable
private fun TermsScreenPreview() {
    PetbulanceTheme {
        TermsScreen(
            navController = rememberNavController(),
            argument = TermsArgument(
                intent = {},
                dataState = TermsDataState.Init,
                screenState = TermsScreenState.Init,
                event = MutableSharedFlow()
            ),
            data = TermsData.empty()
        )
    }
}

@Preview(apiLevel = 34)
@Composable
private fun TermsScreenContentsPreview() {
    PetbulanceTheme {
        TermsScreenContents(
            data = TermsData.stub(),
            onIntent = {}
        )
    }
}