package com.petbulance.presentation.screen.feature.mypage.sections.help.notice.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.petbulance.domain.model.type.NoticeStatusType
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXXS

@Composable
fun NoticeStatusChip(status: NoticeStatusType) {
    val backgroundColor = when (status) {
        NoticeStatusType.EVENT -> colorScheme.tag.blue.bg
        NoticeStatusType.ADVERTISING -> colorScheme.tag.green.bg
        NoticeStatusType.NOTICE -> colorScheme.tag.yellow.subtle
    }

    val textColor = when (status) {
        NoticeStatusType.EVENT -> colorScheme.tag.blue.strong
        NoticeStatusType.ADVERTISING -> colorScheme.tag.green.medium
        NoticeStatusType.NOTICE -> colorScheme.tag.trust.verystrong
    }

    Box(
        modifier = Modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(4.dp))
            .padding(vertical = spacingXXXS, horizontal = spacingXS)
    ) {
        Text(
            text = status.korean,
            color = textColor,
            style = typography.labelMedium.emp()
        )
    }
}