package com.petbulance.presentation.screen.feature.review.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS

@Composable
fun ReviewInfoDialog(onDismissRequest: () -> Unit) {
    BasicDialog(
        backHandler = onDismissRequest
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXL)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(spacingXS)
            ) {
                Text(
                    text = "리뷰 작성 안내",
                    style = typography.titleSmall.emp(),
                    color = colorScheme.text.primary
                )

                Text(
                    text = "펫뷸런스는 진솔한 리뷰만을 모으기 위해 다음과 같은 검수절차를 거치고 있습니다.",
                    style = typography.bodySmall,
                    color = colorScheme.text.tertiary
                )
            }
            ReviewInfoDialogItem(
                index = 1,
                title = "시스템 모니터링 및 검수팀 검수작업",
                desc = "(펫뷸런스 리뷰 정책)"
            )

            ReviewInfoDialogItem(
                index = 2,
                title = "리뷰 등록 완료"
            )

            Text(
                text = "저희는 펫뷸런스 회원들에게 특정 병원을 방문하게 할 목적으로 진료 후기를 작성하게 하거나 유도하지 않음을 안내드립니다. 펫뷸런스의 리뷰는 펫뷸런스 회원분들이 스스로 만들어 나가는 의료정보 공유 문화입니다.",
                style = typography.labelMedium,
                color = colorScheme.text.tertiary
            )

            BasicButton(
                modifier = Modifier.fillMaxWidth().padding(vertical = spacingXS),
                text = "확인",
                size = BasicButtonSize.M,
                buttonType = BasicButtonType.DEFAULT,
                radius = 12.dp,
                onClicked = onDismissRequest
            )
        }
    }
}

@Composable
private fun ReviewInfoDialogItem(
    index: Int,
    title: String,
    desc: String? = null
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingMedium)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = colorScheme.tag.trust.medium,
                    shape = RoundedCornerShape(1000.dp)
                )
        ) {
            Text(
                text = index.toString(),
                style = typography.bodyMedium.emp(),
                color = colorScheme.text.inverse,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Column {
            Text(
                text = title,
                style = typography.bodySmall.emp(),
                color = colorScheme.text.primary
            )
            if (desc != null) {
                Text(
                    text = desc,
                    style = typography.bodySmall.emp(),
                    color = colorScheme.action.primary.default
                )
            }
        }
    }
}

@Preview(apiLevel = 34)
@Composable
private fun ReviewDialogPreview() {
    PetbulanceTheme {
        ReviewInfoDialog({})
    }
}