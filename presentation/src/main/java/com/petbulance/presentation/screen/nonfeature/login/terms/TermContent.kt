package com.petbulance.presentation.screen.nonfeature.login.terms

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.petbulance.domain.model.feature.user.terms.Term
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulancePrimitives
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.atom.*
import com.petbulance.presentation.component.ui.*
import com.petbulance.presentation.utils.HtmlText

@Composable
fun TermsContent(
    data: TermsData,
    onIntent: (TermsIntent) -> Unit,
    onCancel: () -> Unit
) {
    BackHandler(enabled = data.currentTerm != null) {
        onIntent(TermsIntent.OnCloseDetail)
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TermsColumn(
                termsList = data.termsList,
                agreedTermIds = data.agreedTermIds,
                isAllRequiredAgreed = data.isAllRequiredAgreed,
                onDetailVisible = { term -> onIntent(TermsIntent.OnDetailClick(term)) },
                onToggle = { term -> onIntent(TermsIntent.OnToggleTerm(term)) },
                onAgree = { onIntent(TermsIntent.OnAgreeClick) },
                onCancel = onCancel
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
    isAllRequiredAgreed: Boolean,
    onDetailVisible: (Term) -> Unit,
    onToggle: (Term) -> Unit,
    onAgree: () -> Unit,
    onCancel: () -> Unit
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
                onToggle = { onToggle(term) },
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
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clickable { onCancel() }
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