package com.petbulance.presentation.component.ui.molecule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall

/**
 * 섹션별 에러 상태 뷰 컴포넌트
 * 데이터 로드 실패 시 재시도 버튼과 함께 표시
 */
@Composable
fun SectionErrorView(
    message: String,
    onRetry: () -> Unit,
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 새로고침 아이콘 버튼
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(colorScheme.bg.icon.hover)
                    .clickable { onRetry() },
                contentAlignment = Alignment.Center
            ) {
                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Default.Refresh),
                    contentDescription = "Retry",
                    size = 28.dp,
                    tint = colorScheme.icon.basic
                )
            }

            Spacer(modifier = Modifier.height(spacingMedium))

            // 에러 메시지
            Text(
                text = message,
                style = typography.bodyMedium,
                color = colorScheme.text.tertiary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(spacingSmall))

            // 재시도 안내 텍스트
            Text(
                text = "다시 시도해주세요",
                style = typography.bodySmall,
                color = colorScheme.text.caption,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun SectionErrorViewPreview() {
    PetbulanceTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SectionErrorView(
                message = "최근 리뷰를 불러올 수 없습니다",
                onRetry = {},
                height = 200.dp
            )
            SectionErrorView(
                message = "네트워크 연결을 확인해주세요",
                onRetry = {},
                height = 150.dp
            )
        }
    }
}
