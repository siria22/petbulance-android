package com.petbulance.presentation.screen.feature.home.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXS

@Composable
fun HomeScreenEmptyStateUi() {

    val randomNumber = (1..5).random()

    val title = when (randomNumber) {
        1 -> "아직 등록된 후기가 없어요."
        2 -> "진료받은 경험을 공유해 주세요."
        3 -> "아직 등록된 후기가 없어요."
        4 -> "생생한 진료 후기를 남겨주세요."
        5 -> "아직 후기가 없어요."
        else -> "아직 등록된 후기가 없어요."
    }

    val description = when (randomNumber) {
        1 -> "생생한 첫 후기를 남겨보세요!"
        2 -> "보호자님의 경험이 누군가에게 큰 도움이 될 수 있어요."
        3 -> "우리 아이의 진료 경험을 공유해 주세요!"
        4 -> "후기를 작성하면 다른 보호자에게 큰 도움이 되어요!"
        5 -> "보호자님의 경험이 누군가에게 큰 도움이 될 수 있어요."
        else -> "생생한 첫 후기를 남겨보세요!"
    }

    val img = when (randomNumber) {
        1 -> R.drawable.img_empty_state_1
        2 -> R.drawable.img_empty_state_2
        3 -> R.drawable.img_empty_state_3
        4 -> R.drawable.img_empty_state_4
        5 -> R.drawable.img_empty_state_5
        else -> R.drawable.img_empty_state_1
    }

    Column(
        modifier = Modifier
            .padding(vertical = spacingMedium, horizontal = spacingSmall)
            .background(color = colorScheme.bg.frame.default)
            .clip(shape = RoundedCornerShape(16.dp)),
        verticalArrangement = Arrangement.spacedBy(spacingXS),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = img),
            contentDescription = "Empty State",
            modifier = Modifier.size(48.dp)
        )

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.emp(),
            color = colorScheme.text.primary,
        )

        Text(
            text = description,
            style = MaterialTheme.typography.labelSmall,
            color = colorScheme.text.primary,
        )
    }
}

@Preview(apiLevel = 34)
@Composable
private fun HomeScreenEmptyStateUiPreview() {
    PetbulanceTheme {
        HomeScreenEmptyStateUi()
    }
}