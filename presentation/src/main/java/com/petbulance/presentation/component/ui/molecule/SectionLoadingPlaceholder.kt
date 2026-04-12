package com.petbulance.presentation.component.ui.molecule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.atom.CustomGreenLoader
import com.petbulance.presentation.component.ui.spacingMedium

/**
 * 섹션별 로딩 플레이스홀더 컴포넌트
 * 각 섹션(배너, 후기, 게시글 등)의 로딩 상태를 표시
 */
@Composable
fun SectionLoadingPlaceholder(
    modifier: Modifier = Modifier,
    height: Dp = 200.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(colorScheme.bg.frame.default)
            .padding(spacingMedium),
        contentAlignment = Alignment.Center
    ) {
        CustomGreenLoader(size = 48.dp)
    }
}

@Preview
@Composable
private fun SectionLoadingPlaceholderPreview() {
    PetbulanceTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SectionLoadingPlaceholder(height = 150.dp)
            SectionLoadingPlaceholder(height = 200.dp)
            SectionLoadingPlaceholder(height = 250.dp)
        }
    }
}
