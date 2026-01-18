package com.example.presentation.screen.feature.search.info.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.domain.model.type.ReviewSortType
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.molecule.ChipWithIcon
import com.example.presentation.component.ui.spacingSmall
import com.example.presentation.screen.feature.search.info.HospitalInfoIntent
import com.example.presentation.screen.feature.search.info.ReviewUiData

@Composable
fun ReviewHeader(
    reviewData: ReviewUiData,
    onIntent: (HospitalInfoIntent) -> Unit
) {
    val commonPadding = 16.dp
    var isMenuOpened by remember { mutableStateOf(false) }
    val sortOptions = ReviewSortType.entries

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(commonPadding)
            .fillMaxWidth()
    ) {
        Box {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacingSmall)
            ) {
                Row(modifier = Modifier.clickable { isMenuOpened = !isMenuOpened }) {
                    Text(
                        text = reviewData.sortBy.korean,
                        color = PetbulanceTheme.colorScheme.text.primary,
                        style = MaterialTheme.typography.bodyMedium.emp(),
                    )
                    BasicIcon(
                        iconResource = IconResource.Vector(Icons.Default.KeyboardArrowDown),
                        contentDescription = "Drop down menu",
                        size = 20.dp,
                        tint = PetbulanceTheme.colorScheme.text.primary,
                    )
                }

                ChipWithIcon(
                    modifier = Modifier.clickable {
                        onIntent(HospitalInfoIntent.ToggleImageOnly(!reviewData.onlyImage))
                    },
                    text = "사진 후기",
                    iconResource = IconResource.Vector(Icons.Default.CameraAlt),
                    backgroundColor = Color.Transparent,
                    contentColor = if (reviewData.onlyImage) PetbulanceTheme.colorScheme.tag.trust.medium else PetbulanceTheme.colorScheme.tag.trust.bg,
                    borderColor = if (reviewData.onlyImage) PetbulanceTheme.colorScheme.tag.trust.medium else PetbulanceTheme.colorScheme.tag.trust.bg
                )
            }

            DropdownMenu(
                expanded = isMenuOpened,
                onDismissRequest = { isMenuOpened = false },
                shape = RoundedCornerShape(12.dp),
                containerColor = Color.White,
                tonalElevation = 0.dp,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = PetbulanceTheme.colorScheme.border.subtle,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .fillMaxWidth(0.5f)
            ) {
                sortOptions.forEach { selectionOption ->
                    val isSelected = selectionOption == reviewData.sortBy
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = selectionOption.korean,
                                color = PetbulanceTheme.colorScheme.text.primary,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        },
                        onClick = {
                            onIntent(HospitalInfoIntent.ChangeReviewSort(selectionOption))
                            isMenuOpened = false
                        },
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .background(
                                color = if (isSelected) PetbulanceTheme.colorScheme.bg.frame.medium else Color.Transparent,
                                shape = RoundedCornerShape(4.dp)
                            ),
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyReviewView() {
    Text(
        text = "표시할 내용이 없어요",
        color = PetbulanceTheme.colorScheme.text.primary,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        textAlign = TextAlign.Center
    )
}