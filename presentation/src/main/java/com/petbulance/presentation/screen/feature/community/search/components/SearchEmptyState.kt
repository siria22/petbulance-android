package com.petbulance.presentation.screen.feature.community.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.ui.atom.BasicImageBox

@Composable
fun SearchEmptyState(
    searchKeyword: String,
    onCreatePostClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BasicImageBox(
            imageResource = R.drawable.img_empty_state_1,
            size = 120.dp,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "'$searchKeyword'을(를) 찾을 수 없어요.",
            style = MaterialTheme.typography.titleMedium,
            color = PetbulanceTheme.colorScheme.text.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "다른 단어를 검색해주세요.",
            style = MaterialTheme.typography.bodyMedium,
            color = PetbulanceTheme.colorScheme.text.secondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onCreatePostClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PetbulanceTheme.colorScheme.bg.frame.default,
                contentColor = PetbulanceTheme.colorScheme.text.inverse
            )
        ) {
            Text(
                text = "글 작성하러 가기",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
private fun SearchEmptyStatePreview() {
    PetbulanceTheme {
        SearchEmptyState(
            searchKeyword = "햄스터",
            onCreatePostClick = {}
        )
    }
}
