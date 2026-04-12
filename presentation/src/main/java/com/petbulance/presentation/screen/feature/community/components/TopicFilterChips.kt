package com.petbulance.presentation.screen.feature.community.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.domain.model.type.PostCategory
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.spacingXS

@Composable
fun TopicFilterChips(
    selectedTopic: PostCategory?,
    onTopicSelected: (PostCategory?) -> Unit
) {
    val allTopics = listOf(null) + PostCategory.entries

    LazyRow(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(spacingXS)
    ) {
        items(
            items = allTopics,
            key = { it?.name ?: "all" }
        ) { topic ->
            TopicChip(
                topic = topic,
                isSelected = selectedTopic == topic,
                onClick = { onTopicSelected(topic) }
            )
        }
    }
}

@Composable
private fun TopicChip(
    topic: PostCategory?,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        colorScheme.action.primary.default
    } else {
        colorScheme.bg.frame.default
    }

    val textColor = if (isSelected) {
        colorScheme.text.inverse
    } else {
        colorScheme.text.secondary
    }

    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(20.dp)
            )
            .then(
                if (!isSelected) {
                    Modifier.border(
                        width = 1.dp,
                        color = colorScheme.border.verySubtle,
                        shape = RoundedCornerShape(20.dp)
                    )
                } else {
                    Modifier
                }
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = topic?.korean ?: "전체",
            style = MaterialTheme.typography.bodySmall,
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TopicFilterChipsPreview() {
    PetbulanceTheme {
        TopicFilterChips(
            selectedTopic = null,
            onTopicSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TopicFilterChipsSelectedPreview() {
    PetbulanceTheme {
        TopicFilterChips(
            selectedTopic = PostCategory.HEALTH,
            onTopicSelected = {}
        )
    }
}
