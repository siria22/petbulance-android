package com.petbulance.presentation.screen.feature.community.detail.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.atom.BasicDialog
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeMedium
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXL

@Composable
fun PostReportBottomSheet(
    onReportOptionClicked: () -> Unit,
    onDismissRequest: () -> Unit
) {
    BasicDialog(
        backHandler = onDismissRequest,
        position = Alignment.BottomCenter,
        paddingValues = PaddingValues(vertical = spacingXXL, horizontal = spacingXL),
        modifier = Modifier.padding(bottom = 60.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXL)
        ) {
            Text(
                text = "신고하기",
                style = typography.titleMedium.emp(),
                color = colorScheme.text.primary
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingXS),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onReportOptionClicked() }
            ) {
                BasicIcon(
                    iconResource = IconResource.Drawable(R.drawable.ic_incident_reporter),
                    contentDescription = "Report",
                    size = iconSizeMedium,
                    tint = colorScheme.text.tertiary
                )
                Text(
                    text = "게시글 신고",
                    style = typography.bodyLarge.emp(),
                    color = colorScheme.text.tertiary
                )
            }
        }
    }
}

@Preview(apiLevel = 34)
@Composable
private fun PostReportBottomSheetPreview() {
    PetbulanceTheme {
        PostReportBottomSheet({}, {})
    }
}
