package com.petbulance.presentation.screen.feature.review.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
fun ReviewReportReasonDialog(
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
            "명예훼손 및 비방성 내용",
            "의료법 및 관련 법률 위반",
            "플랫폼 이용 정책 위반",
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
                    ReviewReportReasonDialogItem(
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
private fun ReviewReportReasonDialogItem(
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