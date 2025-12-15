package com.example.presentation.screen.feature.search.views.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.BasicInputTextField
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.dropShadow
import com.example.presentation.component.ui.spacingXS

@Composable
fun SearchBar(
    queryString: String,
    onQueryStringChanged: (String) -> Unit,
    onMoveBackIconClicked: () -> Unit,
    onSearchButtonClicked: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .dropShadow(
                shape = RoundedCornerShape(0.dp),
                color = colorScheme.border.subtle,
                blur = 0.dp,
                offsetY = 1.dp
            )
            .background(
                color = colorScheme.bg.frame.default
            )
    ) {
        BasicIcon(
            iconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
            contentDescription = "Move back",
            size = 48.dp,
            modifier = Modifier.clickable {
                onMoveBackIconClicked()
            }
        )
        BasicInputTextField(
            value = queryString,
            onValueChange = { onQueryStringChanged(it) },
            placeholder = "병원 이름을 검색하세요",
            modifier = Modifier.weight(1f),
            onSearchButtonClicked = { onSearchButtonClicked(queryString) }
        )
        Text(
            text = "닫기",
            style = MaterialTheme.typography.labelMedium,
            color = colorScheme.text.secondary,
            modifier = Modifier
                .padding(horizontal = spacingXS)
                .clickable { onMoveBackIconClicked() }
        )
    }
}

@Preview(apiLevel = 34)
@Composable
private fun SearchBarPreview() {
    PetbulanceTheme {
        SearchBar("", {}, {}, {})
    }
}
