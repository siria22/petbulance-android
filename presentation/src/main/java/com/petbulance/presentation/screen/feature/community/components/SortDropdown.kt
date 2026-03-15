package com.petbulance.presentation.screen.feature.community.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource

enum class SortType(val korean: String, val value: String) {
    LATEST("최신순", "latest"),
    POPULAR("인기순", "popular")
}

@Composable
fun SortDropdown(
    modifier: Modifier = Modifier,
    selectedSort: String,
    onSortSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val currentSortType = SortType.entries.find { it.value == selectedSort } ?: SortType.LATEST

    Box(modifier = modifier) {
        Row(
            modifier = Modifier.clickable { expanded = true },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = currentSortType.korean,
                style = MaterialTheme.typography.bodySmall,
                color = PetbulanceTheme.colorScheme.text.secondary
            )
            BasicIcon(
                iconResource = IconResource.Vector(Icons.Default.ArrowDropDown),
                contentDescription = "정렬 선택",
                tint = PetbulanceTheme.colorScheme.icon.dark,
                size = 20.dp
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(PetbulanceTheme.colorScheme.bg.frame.default)
        ) {
            SortType.entries.forEach { sortType ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = sortType.korean,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (currentSortType == sortType)
                                PetbulanceTheme.colorScheme.text.primary
                            else
                                PetbulanceTheme.colorScheme.text.secondary
                        )
                    },
                    onClick = {
                        onSortSelected(sortType.value)
                        expanded = false
                    },
                    modifier = Modifier.background(
                        if (currentSortType == sortType)
                            PetbulanceTheme.colorScheme.bg.frame.subtle
                        else
                            PetbulanceTheme.colorScheme.bg.frame.default
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SortDropdownPreview() {
    PetbulanceTheme {
        SortDropdown(
            selectedSort = "latest",
            onSortSelected = {}
        )
    }
}
