package com.petbulance.presentation.component.ui.molecule

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall

/**
 * "Coming Soon" 플레이스홀더 컴포넌트
 * 아직 구현되지 않은 기능(예: 커뮤니티)을 표시할 때 사용
 */
@Composable
fun ComingSoonPlaceholder(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colorScheme.bg.frame.default)
            .padding(vertical = spacingSmall, horizontal = spacingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacingSmall)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_comming_soon),
            contentDescription = "Coming Soon",
            modifier = Modifier.size(60.dp),
        )

        Text(
            text = "Coming Soon",
            style = typography.bodyLarge.emp(),
            color = colorScheme.text.primary,
            textAlign = TextAlign.Center
        )

        Text(
            text = "커뮤니티 기능 준비중이에요!\n빠른 시일 내에 찾아뵐게요.",
            style = typography.labelMedium,
            color = colorScheme.text.caption,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun ComingSoonPlaceholderPreview() {
    PetbulanceTheme {
        ComingSoonPlaceholder()
    }
}
