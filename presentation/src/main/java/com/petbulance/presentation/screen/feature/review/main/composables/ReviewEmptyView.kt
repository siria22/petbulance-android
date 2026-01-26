package com.petbulance.presentation.screen.feature.review.main.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.spacingLarge
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS

@Composable
fun ReviewEmptyView(
    modifier: Modifier = Modifier,
    title: String = "해당 조건의 병원 후기가 없어요.",
    description: String = "필터를 조정해서 다시 검색해주세요.",
    onReportHospitalClick: () -> Unit = {},
    onAskCommunityClick: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingSmall, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxHeight()
            .padding(vertical = spacingXL)
    ) {
        BasicIcon(
            iconResource = IconResource.Drawable(R.drawable.img_no_result_3),
            contentDescription = "No result",
            size = 160.dp,
            tint = Color.Unspecified
        )
        Text(
            text = title,
            style = typography.titleSmall,
            color = colorScheme.text.tertiary
        )
        Text(
            text = description,
            textAlign = TextAlign.Center,
            style = typography.bodySmall,
            color = colorScheme.text.tertiary
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXS),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = spacingMedium, vertical = spacingLarge)
        ) {
            BasicButton(
                modifier = Modifier.fillMaxWidth(),
                text = "새로운 병원 제보하기",
                size = BasicButtonSize.M,
                buttonType = BasicButtonType.SECONDARY,
                radius = 12.dp,
                onClicked = onReportHospitalClick
            )
            BasicButton(
                modifier = Modifier.fillMaxWidth(),
                text = "커뮤니티에 질문하기",
                size = BasicButtonSize.M,
                buttonType = BasicButtonType.SECONDARY,
                radius = 12.dp,
                onClicked = onAskCommunityClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewEmptyViewPreview() {
    ReviewEmptyView()
}