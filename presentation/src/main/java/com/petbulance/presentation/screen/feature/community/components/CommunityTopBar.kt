package com.petbulance.presentation.screen.feature.community.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
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
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.spacingMedium

@Composable
fun CommunityTopBar(
    modifier: Modifier = Modifier,
    selectedAnimalType: AnimalCategory?,
    onAnimalTypeSelected: (AnimalCategory?) -> Unit,
    onSearchClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(PetbulanceTheme.colorScheme.bg.frame.default)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .clickable { expanded = true }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = selectedAnimalType?.korean ?: "전체",
                    style = MaterialTheme.typography.bodyLarge.emp(),
                    color = PetbulanceTheme.colorScheme.text.primary
                )
                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Default.ArrowDropDown),
                    contentDescription = "동물종 선택",
                    tint = PetbulanceTheme.colorScheme.icon.dark
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingMedium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Default.Search),
                    contentDescription = "검색",
                    tint = PetbulanceTheme.colorScheme.icon.basic,
                    modifier = Modifier.clickable(onClick = onSearchClick)
                )
                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Default.Notifications),
                    contentDescription = "알림",
                    tint = PetbulanceTheme.colorScheme.icon.basic,
                    modifier = Modifier.clickable(onClick = onNotificationClick)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(PetbulanceTheme.colorScheme.bg.frame.default)
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "전체",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (selectedAnimalType == null) 
                            PetbulanceTheme.colorScheme.text.primary 
                        else 
                            PetbulanceTheme.colorScheme.text.secondary
                    )
                },
                onClick = {
                    onAnimalTypeSelected(null)
                    expanded = false
                },
                modifier = Modifier.background(
                    if (selectedAnimalType == null) 
                        PetbulanceTheme.colorScheme.bg.frame.subtle
                    else 
                        PetbulanceTheme.colorScheme.bg.frame.default
                )
            )

            AnimalCategory.entries.filter { it != AnimalCategory.ALL }.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = category.korean,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (selectedAnimalType == category) 
                                PetbulanceTheme.colorScheme.text.primary 
                            else 
                                PetbulanceTheme.colorScheme.text.secondary
                        )
                    },
                    onClick = {
                        onAnimalTypeSelected(category)
                        expanded = false
                    },
                    modifier = Modifier.background(
                        if (selectedAnimalType == category) 
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
private fun CommunityTopBarPreview() {
    PetbulanceTheme {
        Column {
            CommunityTopBar(
                selectedAnimalType = null,
                onAnimalTypeSelected = {},
                onSearchClick = {},
                onNotificationClick = {}
            )
            CommunityTopBar(
                selectedAnimalType = AnimalCategory.SMALLMAMMALS,
                onAnimalTypeSelected = {},
                onSearchClick = {},
                onNotificationClick = {}
            )
        }
    }
}
