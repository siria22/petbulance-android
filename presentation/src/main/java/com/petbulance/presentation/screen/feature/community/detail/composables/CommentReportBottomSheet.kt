package com.petbulance.presentation.screen.feature.community.detail.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeMS
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXS

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentReportBottomSheet(
    onReportOptionClicked: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = colorScheme.bg.frame.default
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = spacingSmall)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onReportOptionClicked() }
                    .padding(horizontal = spacingMedium, vertical = spacingMedium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Outlined.Report),
                    contentDescription = "신고",
                    size = iconSizeMS,
                    tint = colorScheme.icon.light,
                    modifier = Modifier.padding(end = spacingXS)
                )
                Text(
                    text = "댓글 신고",
                    style = typography.bodyLarge,
                    color = colorScheme.text.primary
                )
            }
        }
    }
}
