package com.petbulance.presentation.component.ui.atom

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.LargeRoundedCorner
import com.petbulance.presentation.component.ui.spacingLarge
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXXL

/**
 * Creates a basic dialog with a transparent background and a content area.
 * It is designed to be used as a simple dialog for user interaction.
 *
 * @param modifier Modifier to be applied to the root layout of the dialog.
 * @param minimumWidth The minimum width of the dialog as a fraction of the screen width.
 * @param backHandler A function that will be called when the back button is pressed.
 * @param content The @Composable content of the dialog.
 */
@Composable
fun BasicDialog(
    modifier: Modifier = Modifier,
    minimumWidth: Float = 0.8f,
    backHandler: () -> Unit = {},
    position: Alignment = Alignment.Center,
    paddingValues: PaddingValues = PaddingValues(horizontal = 16.dp),
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = 0.5f))
            .clickable(
                enabled = true,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                backHandler()
            }
            .padding(paddingValues)
    ) {
        Column(
            modifier = modifier
                .align(position)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    // Do nothing to prevent triggering the parent's clickable (backHandler)
                }
                .background(
                    color = Color.White,
                    shape = LargeRoundedCorner
                )
                .padding(horizontal = spacingXL)
                .padding(top = spacingXXL, bottom = spacingXL)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacingXL)
        ) {
            content()
        }
        BackHandler { backHandler() }
    }
}

@Preview(apiLevel = 34)
@Composable
private fun BasicDialogPreview() {
    PetbulanceTheme {
        BasicDialog(
            backHandler = {},
            paddingValues = PaddingValues(horizontal = spacingXL, vertical = spacingXXL)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(spacingLarge),
            ) {
                Text(
                    text = "병원 검색을 이용하려면\n위치정보 이용 약관 동의가 필요해요",
                    style = MaterialTheme.typography.titleSmall.emp(),
                    color = PetbulanceTheme.colorScheme.text.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "(선택)위치 정보 이용 약관",
                        style = MaterialTheme.typography.labelLarge,
                        color = PetbulanceTheme.colorScheme.text.caption,
                    )
                    Text(
                        text = "약관보기",
                        style = MaterialTheme.typography.labelLarge,
                        color = PetbulanceTheme.colorScheme.action.link.default,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            /* show terms */
                        }
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BasicButton(
                        text = "다음에",
                        size = BasicButtonSize.L,
                        buttonType = BasicButtonType.SECONDARY,
                        radius = 28.dp,
                        onClicked = { }
                    )
                    BasicButton(
                        text = "다음에",
                        size = BasicButtonSize.L,
                        buttonType = BasicButtonType.PRIMARY,
                        radius = 28.dp,
                        onClicked = { }
                    )
                }
            }
        }
    }
}