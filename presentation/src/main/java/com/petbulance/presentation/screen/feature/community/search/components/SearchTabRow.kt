package com.petbulance.presentation.screen.feature.community.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp

enum class SearchTab(val korean: String) {
    POST("게시글"),
    COMMENT("댓글")
}

@Composable
fun SearchTabRow(
    selectedTab: SearchTab,
    onTabSelected: (SearchTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedColor = colorScheme.action.primary.default
    val unselectedColor = colorScheme.text.disabled

    TabRow(
        selectedTabIndex = selectedTab.ordinal,
        modifier = modifier
            .fillMaxWidth()
            .background(colorScheme.bg.frame.default),
        containerColor = colorScheme.bg.frame.default,
        contentColor = colorScheme.text.primary,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                color = selectedColor
            )
        }
    ) {
        SearchTab.entries.forEach { tab ->
            Tab(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                text = {
                    Text(
                        text = tab.korean,
                        style = MaterialTheme.typography.bodySmall.emp(),
                        color = if (selectedTab == tab) {
                            selectedColor
                        } else {
                            unselectedColor
                        }
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun SearchTabRowPreview() {
    PetbulanceTheme {
        SearchTabRow(
            selectedTab = SearchTab.POST,
            onTabSelected = {}
        )
    }
}

@Preview
@Composable
private fun SearchTabRowCommentPreview() {
    PetbulanceTheme {
        SearchTabRow(
            selectedTab = SearchTab.COMMENT,
            onTabSelected = {}
        )
    }
}
