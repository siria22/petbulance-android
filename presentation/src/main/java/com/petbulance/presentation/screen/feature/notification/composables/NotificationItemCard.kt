package com.petbulance.presentation.screen.feature.notification.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.petbulance.domain.model.feature.notification.NotificationItem
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.CommonDivider
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXS

@Composable
fun NotificationItemCard(
    item: NotificationItem,
    onClick: () -> Unit
) {
    val bgColor = if (item.isRead) {
        colorScheme.bg.frame.default
    } else {
        colorScheme.bg.frame.subtle
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(horizontal = spacingMedium, vertical = spacingSmall)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.topic,
                style = typography.labelSmall,
                color = colorScheme.text.caption
            )
            Text(
                text = item.createdAt,
                style = typography.labelSmall,
                color = colorScheme.text.caption
            )
        }

        Text(
            text = item.message,
            style = typography.bodyMedium,
            color = colorScheme.text.primary,
            modifier = Modifier.padding(top = spacingXS)
        )
    }
    CommonDivider()
}
