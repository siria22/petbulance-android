package com.petbulance.presentation.screen.feature.community.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.BasicInputTextField
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.dropShadow
import com.petbulance.presentation.component.ui.iconSizeMS

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    searchKeyword: String,
    onSearchKeywordChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .dropShadow(
                shape = RoundedCornerShape(0.dp),
                color = colorScheme.border.subtle,
                blur = 0.dp,
                offsetY = 1.dp
            )
            .background(color = colorScheme.bg.frame.default)
    ) {
        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            BasicIcon(
                iconResource = IconResource.Vector(Icons.Default.ArrowBackIosNew),
                contentDescription = "뒤로가기",
                size = iconSizeMS,
                tint = colorScheme.icon.dark,
                modifier = Modifier
                    .clickable(onClick = onBackClick)
                    .padding(16.dp)
            )
        }

        BasicInputTextField(
            value = searchKeyword,
            onValueChange = onSearchKeywordChange,
            modifier = Modifier.weight(1f),
            placeholder = "검색어를 입력하세요",
            textStyle = MaterialTheme.typography.bodyLarge,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            onSearchButtonClicked = onSearchClick
        )
        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "검색",
                style = MaterialTheme.typography.bodySmall.emp(),
                color = colorScheme.text.secondary,
                modifier = Modifier.clickable(onClick = onSearchClick)
            )
        }
    }
}

@Preview
@Composable
private fun SearchTopBarPreview() {
    PetbulanceTheme {
        SearchTopBar(
            searchKeyword = "햄스터",
            onSearchKeywordChange = {},
            onSearchClick = {},
            onBackClick = {},
        )
    }
}

@Preview
@Composable
private fun SearchTopBarEmptyPreview() {
    PetbulanceTheme {
        SearchTopBar(
            searchKeyword = "",
            onSearchKeywordChange = {},
            onSearchClick = {},
            onBackClick = {},
        )
    }
}
