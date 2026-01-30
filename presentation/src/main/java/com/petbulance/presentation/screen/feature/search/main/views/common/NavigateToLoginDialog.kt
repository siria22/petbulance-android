package com.petbulance.presentation.screen.feature.search.main.views.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.BasicDialog
import com.petbulance.presentation.component.ui.spacingLarge

@Composable
fun NavigateToLoginDialog(
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit
) {
    BasicDialog(
        backHandler = onDismissRequest,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "로그인하고 바로 확인하세요",
                style = typography.titleSmall,
                color = colorScheme.text.primary
            )

            BasicButton(
                text = "로그인하기",
                size = BasicButtonSize.L,
                buttonType = BasicButtonType.PRIMARY,
                radius = 28.dp,
                onClicked = onConfirm,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Preview
@Composable
private fun NavigateToLoginDialogPreview() {
    NavigateToLoginDialog(onConfirm = {}, onDismissRequest = {})
}