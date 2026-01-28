package com.petbulance.presentation.screen.feature.review.create.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.spacingLarge
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS

@Composable
fun ReceiptVerifiedCard() {
    Row(
        modifier = Modifier.padding(horizontal = spacingLarge, vertical = spacingXL),
        horizontalArrangement = Arrangement.spacedBy(spacingXS),
    ) {
        BasicIcon(
            iconResource = IconResource.Vector(Icons.Default.CheckCircle),
            contentDescription = "CircleCheck",
            size = 36.dp,
            tint = PetbulanceTheme.colorScheme.tag.trust.medium
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXS),
        ) {
            Text(
                text = "영수증 인증 완료",
                style = MaterialTheme.typography.titleSmall,
                color = PetbulanceTheme.colorScheme.text.primary
            )
            Text(
                text = "이제 후기를 작성할 수 있습니다.",
                style = MaterialTheme.typography.bodySmall,
                color = PetbulanceTheme.colorScheme.text.caption
            )
        }
    }
}