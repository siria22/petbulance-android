package com.petbulance.presentation.screen.feature.community.search.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeMS
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXL

@Composable
fun SearchInputView(
    searchKeyword: String,
    recentKeywords: List<String>,
    onSearchKeywordChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onRecentKeywordClick: (String) -> Unit,
    onDeleteKeyword: (String) -> Unit,
    onDeleteAllKeywords: () -> Unit,
    onRestoreKeywords: (List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    var deletedKeywords by remember { mutableStateOf<List<String>?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.bg.frame.default)
                .padding(horizontal = spacingXL, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(spacingSmall)
        ) {
            if (recentKeywords.isEmpty()) {
                Text(
                    text = "최근 검색어",
                    style = typography.bodyMedium.emp(),
                    color = colorScheme.text.primary,
                )
                Text(
                    text = "검색 시 자동으로 검색어가 저장돼요",
                    style = typography.bodyMedium,
                    color = colorScheme.text.caption
                )
            }

            if (recentKeywords.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "최근 검색어",
                        style = typography.bodyMedium.emp(),
                        color = colorScheme.text.primary
                    )
                    Text(
                        text = "전체 삭제",
                        style = typography.labelMedium.emp(),
                        color = colorScheme.text.secondary,
                        modifier = Modifier.clickable {
                            onDeleteAllKeywords()
                        }
                    )
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = recentKeywords,
                        key = { it }
                    ) { keyword ->
                        RecentKeywordItem(
                            keyword = keyword,
                            onClick = { onRecentKeywordClick(keyword) },
                            onDeleteClick = { onDeleteKeyword(keyword) }
                        )
                    }
                }
            }
        }

        // 토스트 팝업 (하단)
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp) // GNB 위에 표시
        )
    }
}

@Composable
private fun RecentKeywordItem(
    keyword: String,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = spacingSmall),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingSmall),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            BasicIcon(
                iconResource = IconResource.Vector(Icons.Outlined.Schedule),
                contentDescription = "최근 검색어",
                tint = colorScheme.icon.light,
                modifier = Modifier.size(20.dp)
            )

            Text(
                text = keyword,
                style = typography.bodyMedium,
                color = colorScheme.text.primary
            )
        }

        BasicIcon(
            iconResource = IconResource.Vector(Icons.Default.Clear),
            contentDescription = "삭제",
            tint = colorScheme.icon.light,
            modifier = Modifier
                .size(iconSizeMS)
                .clickable(onClick = onDeleteClick)
        )
    }
}


@Preview
@Composable
private fun SearchInputViewEmptyPreview() {
    PetbulanceTheme {
        SearchInputView(
            searchKeyword = "",
            recentKeywords = listOf("ㅇㄴㅁㄹ", "렁렁러"),
            onSearchKeywordChange = {},
            onSearchClick = {},
            onRecentKeywordClick = {},
            onDeleteKeyword = {},
            onDeleteAllKeywords = {},
            onRestoreKeywords = {}
        )
    }
}

@Preview
@Composable
private fun RecentKeywordItemPreview() {
    PetbulanceTheme {
        RecentKeywordItem(
            keyword = "햄스터 건강",
            onClick = {},
            onDeleteClick = {}
        )
    }
}
