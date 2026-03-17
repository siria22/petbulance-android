package com.petbulance.presentation.screen.feature.community.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import com.petbulance.domain.model.type.SearchScope
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource

@Composable
fun SearchScopeDropdown(
    selectedScope: String,
    isCommentTab: Boolean,
    onScopeSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val availableScopes = if (isCommentTab) {
        SearchScope.getCommentSearchScopes()
    } else {
        SearchScope.getPostSearchScopes()
    }

    val currentScope = availableScopes.find { it.value == selectedScope }
        ?: availableScopes.first()

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .clickable { expanded = true }
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = currentScope.korean,
                style = MaterialTheme.typography.bodyMedium,
                color = PetbulanceTheme.colorScheme.text.secondary
            )
            BasicIcon(
                iconResource = IconResource.Vector(Icons.Default.ArrowDropDown),
                contentDescription = "검색 대상 선택",
                tint = PetbulanceTheme.colorScheme.icon.dark
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(PetbulanceTheme.colorScheme.bg.frame.default)
        ) {
            availableScopes.forEach { scope ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = scope.korean,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (scope.value == selectedScope) {
                                PetbulanceTheme.colorScheme.text.primary
                            } else {
                                PetbulanceTheme.colorScheme.text.secondary
                            }
                        )
                    },
                    onClick = {
                        onScopeSelected(scope.value)
                        expanded = false
                    },
                    modifier = Modifier.background(
                        if (scope.value == selectedScope) {
                            PetbulanceTheme.colorScheme.bg.frame.subtle
                        } else {
                            PetbulanceTheme.colorScheme.bg.frame.default
                        }
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun SearchScopeDropdownPostPreview() {
    PetbulanceTheme {
        SearchScopeDropdown(
            selectedScope = "title_content",
            isCommentTab = false,
            onScopeSelected = {}
        )
    }
}

@Preview
@Composable
private fun SearchScopeDropdownCommentPreview() {
    PetbulanceTheme {
        SearchScopeDropdown(
            selectedScope = "content",
            isCommentTab = true,
            onScopeSelected = {}
        )
    }
}
