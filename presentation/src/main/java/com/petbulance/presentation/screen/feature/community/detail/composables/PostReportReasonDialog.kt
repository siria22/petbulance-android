package com.petbulance.presentation.screen.feature.community.detail.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.BasicDialog
import com.petbulance.presentation.component.ui.atom.CustomRadioButton
import com.petbulance.presentation.component.ui.iconSizeMedium
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXL
import com.petbulance.presentation.component.ui.spacingXXS

@Composable
fun PostReportReasonDialog(
    selectedReason: String,
    onReasonClicked: (String) -> Unit,
    onSubmitClicked: () -> Unit,
    onDismissRequest: () -> Unit
) {
    BasicDialog(
        backHandler = onDismissRequest,
        paddingValues = PaddingValues(horizontal = spacingXL, vertical = spacingXXL),
    ) {
        val reasonList = listOf(
            "욕설/비방/혐오 표현",
            "음란물/성적 콘텐츠",
            "도배/광고",
            "허위/가짜 정보",
            "저작권 침해",
            "개인정보 노출",
            "사기/금융 범죄",
            "기타"
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXL)
        ) {
            Text(
                text = "후기를 신고한 이유를 알려주세요.",
                style = typography.titleMedium.emp(),
                color = colorScheme.text.primary
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(spacingXS),
            ) {
                reasonList.forEach { item ->
                    PostReportReasonDialogItem(
                        content = item,
                        isSelected = selectedReason == item,
                        onClicked = { onReasonClicked(item) }
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingXS),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicButton(
                    text = "취소",
                    size = BasicButtonSize.M,
                    buttonType = BasicButtonType.DEFAULT,
                    radius = 12.dp,
                    onClicked = onDismissRequest,
                    modifier = Modifier.weight(1f)
                )

                val submitButtonType = if (selectedReason.isNullOrBlank()) {
                    BasicButtonType.DISABLED
                } else {
                    BasicButtonType.SECONDARY
                }

                BasicButton(
                    text = "제출",
                    size = BasicButtonSize.M,
                    buttonType = submitButtonType,
                    radius = 12.dp,
                    onClicked = {
                        if (selectedReason.isNotBlank()) {
                            onSubmitClicked()
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun PostReportReasonDialogItem(
    content: String,
    isSelected: Boolean,
    onClicked: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingXXS),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomRadioButton(
            selected = isSelected,
            onClick = onClicked,
            size = iconSizeMedium
        )
        Text(
            text = content,
            style = typography.bodyMedium,
            color = colorScheme.text.primary
        )
    }
}

@Preview(apiLevel = 34)
@Composable
private fun PostReportReasonDialogPreview() {
    PetbulanceTheme {
        PostReportReasonDialog(
            selectedReason = "욕설/비방/혐오 표현",
            onReasonClicked = {},
            onSubmitClicked = {},
            onDismissRequest = {}
        )
    }
}
