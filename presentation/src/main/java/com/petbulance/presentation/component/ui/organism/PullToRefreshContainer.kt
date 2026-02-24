package com.petbulance.presentation.component.ui.organism

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.atom.CustomGreenLoader
import com.petbulance.presentation.component.ui.pullToRefreshDelayMs
import com.petbulance.presentation.component.ui.pullToRefreshMaxOffsetMultiplier
import com.petbulance.presentation.component.ui.pullToRefreshTextThresholdMultiplier
import com.petbulance.presentation.component.ui.pullToRefreshThreshold
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall

/**
 * Pull-to-Refresh 컨테이너 컴포넌트
 * 스크롤 가능한 컨텐츠에 Pull-to-Refresh 기능을 추가합니다.
 *
 * @param scrollState 스크롤 상태 (최상단 감지용)
 * @param onRefresh 새로고침 트리거 시 실행될 콜백
 * @param content 스크롤 가능한 컨텐츠
 */
@Composable
fun PullToRefreshContainer(
    scrollState: ScrollState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var pullOffset by remember { mutableFloatStateOf(0f) }
    var isRefreshing by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val pullThreshold = with(density) { pullToRefreshThreshold.toPx() }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            onRefresh()
            kotlinx.coroutines.delay(pullToRefreshDelayMs)
            isRefreshing = false
            pullOffset = 0f
        }
    }

    Box(
        modifier = modifier.pointerInput(Unit) {
            detectVerticalDragGestures(
                onDragEnd = {
                    if (pullOffset >= pullThreshold && scrollState.value == 0) {
                        isRefreshing = true
                    } else {
                        pullOffset = 0f
                    }
                },
                onVerticalDrag = { _, dragAmount ->
                    if (scrollState.value == 0 && dragAmount > 0) {
                        pullOffset = (pullOffset + dragAmount).coerceIn(0f, pullThreshold * pullToRefreshMaxOffsetMultiplier)
                    }
                }
            )
        }
    ) {
        content()

        if (pullOffset > 0f || isRefreshing) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((pullOffset / 2).dp)
                    .align(Alignment.TopCenter),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = colorScheme.bg.frame.default,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = spacingMedium, vertical = spacingSmall),
                    contentAlignment = Alignment.Center
                ) {
                    if (isRefreshing) {
                        CustomGreenLoader(size = 32.dp)
                    } else if (pullOffset > pullThreshold * pullToRefreshTextThresholdMultiplier) {
                        Text(
                            text = if (pullOffset >= pullThreshold) "놓아서 새로고침" else "당겨서 새로고침",
                            style = MaterialTheme.typography.bodySmall,
                            color = colorScheme.text.tertiary
                        )
                    }
                }
            }
        }
    }
}
