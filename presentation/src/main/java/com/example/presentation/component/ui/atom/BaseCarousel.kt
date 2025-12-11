package com.example.presentation.component.ui.atom

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.PetbulancePrimitives

/**
 * 공통 캐러셀 컴포저블
 *
 * @param T 캐러셀에서 표시할 아이템의 타입
 * @param modifier Modifier
 * @param items 캐러셀에 표시할 아이템 리스트
 * @param state PagerState
 * @param contentPadding 캐러셀의 시작/끝 부분에 적용할 패딩
 * @param itemSpacing 각 아이템 사이의 간격
 * @param isIndicatorVisible 인디케이터 표시 여부
 * @param itemContent 각 아이템을 표시할 Composable
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> BaseCarousel(
    modifier: Modifier = Modifier,
    items: List<T>,
    state: PagerState = rememberPagerState { items.size },
    contentPadding: PaddingValues = PaddingValues(0.dp),
    itemSpacing: Dp = 0.dp,
    isIndicatorVisible: Boolean = false,
    itemContent: @Composable (page: Int, item: T) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = state,
            contentPadding = contentPadding,
            pageSpacing = itemSpacing,
        ) { page ->
            itemContent(page, items[page])
        }
        if (isIndicatorVisible) {
            CarouselIndicator(
                pageCount = state.pageCount,
                currentPage = state.currentPage
            )
        }
    }
}

@Composable
fun CarouselIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = PetbulancePrimitives.Gray.p800,
    inactiveColor: Color = PetbulancePrimitives.Gray.p300,
    dotSize: Dp = 8.dp,
    spacing: Dp = 12.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        for (i in 0 until pageCount) {
            val color = if (i == currentPage) activeColor else inactiveColor
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BaseCarouselPreview() {
    val items = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
    BaseCarousel(
        items = items,
        contentPadding = PaddingValues(horizontal = 20.dp),
        itemSpacing = 16.dp,
        isIndicatorVisible = true
    ) { _, item ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.LightGray)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = item)
        }
    }
}