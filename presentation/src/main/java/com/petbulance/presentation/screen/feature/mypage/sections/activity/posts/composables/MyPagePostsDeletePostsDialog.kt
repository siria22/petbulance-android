package com.petbulance.presentation.screen.feature.mypage.sections.activity.posts.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
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
import com.petbulance.presentation.component.ui.atom.BasicDialog
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeMedium
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXL

@Composable
fun MyPagePostsDeletePostsDialog(
    onDeleteOptionClicked: () -> Unit,
    onDismissRequest: () -> Unit
) {
    BasicDialog(
        backHandler = onDismissRequest,
        position = Alignment.Center,
        paddingValues = PaddingValues(vertical = spacingXXL, horizontal = spacingXL),
        modifier = Modifier.padding(bottom = 60.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXL)
        ) {
            Text(
                text = "커뮤니티 게시글 삭제",
                style = typography.titleMedium.emp(),
                color = colorScheme.text.primary
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingXS),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDeleteOptionClicked() }
            ) {
                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Outlined.Delete),
                    contentDescription = "Delete",
                    size = iconSizeMedium,
                    tint = colorScheme.text.caption
                )
                Text(
                    text = "선택 삭제",
                    style = typography.bodyLarge,
                    color = colorScheme.text.caption
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MyPagePostsDeletePostsDialogPreview() {
    PetbulanceTheme {
        MyPagePostsDeletePostsDialog(
            onDeleteOptionClicked = {},
            onDismissRequest = {}
        )
    }
}
