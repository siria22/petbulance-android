package com.petbulance.presentation.screen.feature.mypage.sections.activity.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.BasicCheckBox
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXS

@Composable
fun ActivitySelectionControlBar(
    isAllSelected: Boolean,
    hasSelection: Boolean,
    onSelectAllClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacingMedium, vertical = spacingXS),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingXXS),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onSelectAllClick() }
        ) {
            BasicCheckBox(
                checkState = isAllSelected,
                onCheckedChange = onSelectAllClick
            )
            Text(
                text = "전체선택",
                style = typography.bodyMedium,
                color = colorScheme.text.secondary
            )
        }

        BasicButton(
            text = "삭제",
            size = BasicButtonSize.S,
            buttonType = BasicButtonType.DEFAULT,
            onClicked = { if (hasSelection) onDeleteClick() }
        )
    }
}
